<script setup>
import { Edit, Delete } from "@element-plus/icons-vue";

import { ref } from "vue";

//文章分类数据模型
const categorys = ref([]);
const categorys_firstname = ref([]);
const categorys_secondname = ref([]);
const categorys_thirdname = ref([]);
//用户搜索时选中的分类id
const categoryId = ref("");
//分类名
const category_first = ref("");
const category_second = ref("");
const category_third = ref("");
//添加/更新名
const category_first_add = ref("");
const category_second_add = ref("");
const category_third_add = ref("");
//用户搜索时选中的发布状态
const state = ref("");

//文章列表数据模型
const articles = ref([]);

//分页条数据模型
const pageNum = ref(1); //当前页
const total = ref(20); //总条数
const pageSize = ref(3); //每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
  pageSize.value = size;
  articleList();
};
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
  pageNum.value = num;
  articleList();
};

//回写文章分类
import {
  articleCategoryListService,
  articleListService,
  articleAddService,
  articleDeleteService,
  articleUpdateService
} from "@/api/article.js";
const articleCategoryList = async () => {
  let result = await articleCategoryListService();
  categorys.value = result.data;
  const first = {};
  const second = {};
  const third = {};
  for (let i = 0; i < result.data.length; i++) {
    let category = result.data[i];
    if(!first[category.firstCategoryName]){
      first[category.firstCategoryName] ={};
    }
    if(!second[category.secondCategoryName]){
      second[category.secondCategoryName] = {
        firstCategoryName: category.firstCategoryName,
      };
    }
    if(!third[category.thirdCategoryName]){
      third[category.thirdCategoryName] = {
        secondCategoryName: category.secondCategoryName,
      };
    }
  }
  categorys_firstname.value = Object.keys(first).map(firstname => ({  
    firstCategoryName: firstname, // 使用 firstname 作为 value（如果需要）  
  }));  
  categorys_secondname.value = Object.keys(second).map(secondname => ({  
    firstCategoryName:second[secondname].firstCategoryName,
    secondCategoryName: secondname, // 使用 firstname 作为 value（如果需要）  
  })); 
  categorys_thirdname.value = Object.keys(third).map(thirdname => ({  
    secondCategoryName:third[thirdname].secondCategoryName,
    thirdCategoryName: thirdname, // 使用 firstname 作为 value（如果需要）  
  })); 
};
const findCategoryID = () => {
  for(let i = 0; i < categorys.value.length; i++){
    let category = categorys.value[i];
    if(category_first.value == category.firstCategoryName && 
    category_second.value == category.secondCategoryName && 
    category_third.value == category.thirdCategoryName){
      categoryId.value = category.id;
      break;
    }
  }
};
const findCategoryIDAdd = () => {
  for(let i = 0; i < categorys.value.length; i++){
    let category = categorys.value[i];
    if(category_first_add.value == category.firstCategoryName && 
    category_second_add.value == category.secondCategoryName && 
    category_third_add.value == category.thirdCategoryName){
      categoryId.value = category.id;
      break;
    }
  }
};
//获取文章列表数据
const articleList = async () => {
  let params = {
    pageNum: pageNum.value,
    pageSize: pageSize.value,
    categoryId: categoryId.value ? categoryId.value : null,
    state: state.value ? state.value : null,
  };
  let result = await articleListService(params);
  //渲染视图
  total.value = result.data.total;
  articles.value = result.data.items;
  //处理数据
  for (let i = 0; i < articles.value.length; i++) {
    let article = articles.value[i];
    for (let j = 0; j < categorys.value.length; j++) {
      if (article.categoryId == categorys.value[j].id) {
        article.firstCategoryName = categorys.value[j].firstCategoryName;
        article.secondCategoryName = categorys.value[j].secondCategoryName;
        article.thirdCategoryName = categorys.value[j].thirdCategoryName;
      }
    }
  }
};

articleCategoryList();
articleList();

import { QuillEditor } from "@vueup/vue-quill";
import "@vueup/vue-quill/dist/vue-quill.snow.css";
import { Plus } from "@element-plus/icons-vue";
import { ElMessage,ElMessageBox } from "element-plus";
//控制抽屉是否显示
const visibleDrawer = ref(false);
//添加表单数据模型
const articleModel = ref({
  title: "",
  categoryId: "",
  content: "",
  state: "",
});
//添加文章
const addArticle = async(clickState)=>{
    //把发布状态赋值给数据模型
    articleModel.value.state = clickState;
    articleModel.value.categoryId =categoryId.value;
    //调用接口
    let result = await articleAddService(articleModel.value);
    ElMessage.success(result.msg?result.msg:'添加成功');
    //抽屉消失
    visibleDrawer.value = false;
    clearCategoryAdd();    
    //刷新当前列表
    articleList()
}
//删除文章
const deleteArticle = async(row)=>{
   //提示用户  确认框
   ElMessageBox.confirm(
    "确认删除该分类吗",
    "温馨提示",
    {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning",
    }
  )
    .then(async () => {
        //调用接口
        let result = await articleDeleteService(row.id);
      ElMessage({
        type: "success",
        message: "删除成功",
      });
      //刷新列表
      articleList();
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "用户取消删除",
      });
    });
}
//定义变量控制标题展示
const title = ref("");
//编辑文章
const showArticle = (row)=>{
  visibleDrawer.value=true;
  title.value='编辑文章';
  articleModel.value.id = row.id;
  articleModel.value.categoryId = row.categoryId;
  articleModel.value.title = row.title;
  for(let i=0; i < categorys.value.length; i++){
    let category = categorys.value[i];
    if(category.id == articleModel.value.categoryId){
      category_first_add.value = category.firstCategoryName;
      category_second_add.value = category.secondCategoryName;
      category_third_add.value = category.thirdCategoryName;
    }
  }
  articleModel.value.content = row.content;
  articleModel.value.state = row.state;
}
const updateArticle = async(clickState)=>{
  articleModel.value.state = clickState;
  articleModel.value.categoryId =categoryId.value;
  let result = await articleUpdateService(articleModel.value);
  ElMessage.success(result.msg?result.msg:'添加成功');
  visibleDrawer.value = false;
  clearCategoryAdd();
  //刷新当前列表
  articleList()
}
const clearData = ()=>{
  articleModel.value.categoryId = '';
  articleModel.value.title = '';
  articleModel.value.state = '';
  articleModel.value.content = '';
}
const clearCategory = ()=>{
  category_first.value = '';
  category_second.value = '';
  category_third.value = '';
  categoryId.value = '';
}
const clearCategoryAdd = ()=>{
  category_first_add.value = '';
  category_second_add.value ='';
  category_third_add.value ='';
  categoryId.value = '';
}
</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文章管理</span>
        <div class="extra">
          <el-button type="primary" @click="visibleDrawer = true; title = '添加文章';clearData()">添加文章</el-button>
        </div>
      </div>
    </template>
    <!-- 搜索表单 -->
    <el-form inline>
      <el-form-item label="文章一级分类：">
        <el-select
          placeholder="请选择"
          v-model="category_first"
          style="width: 100px"
          @change="category_second='';category_third=''"
        >
          <el-option
            v-for="c in categorys_firstname"
            :key="c.id"
            :label="c.firstCategoryName"
            :value="c.firstCategoryName"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文章二级分类：">
        <el-select
          placeholder="请选择"
          v-model="category_second"
          style="width: 100px"
          :disabled = "category_first.length === 0"
          @change="category_third=''"
        >
          <el-option
            v-for="c in categorys_secondname"
            :key="c.id"
            :label="c.secondCategoryName"
            :value="c.secondCategoryName"
            v-show="c.firstCategoryName == category_first"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="文章三级分类：">
        <el-select
          placeholder="请选择"
          v-model="category_third"
          style="width: 100px"
          :disabled = "category_second.length === 0 || category_second === ''"
        >
          <el-option
            v-for="c in categorys_thirdname"
            :key="c.id"
            :label="c.thirdCategoryName"
            :value="c.thirdCategoryName"
            v-show="c.secondCategoryName === category_second"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="发布状态：">
        <el-select placeholder="请选择" v-model="state" style="width: 200px">
          <el-option label="已发布" value="已发布"></el-option>
          <el-option label="草稿" value="草稿"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="findCategoryID();articleList()">搜索</el-button>
        <el-button
          @click="
            categoryId = '';
            state = '';
            category_first = '';
            category_second= '';
            category_third = '';
            articleList();
          "
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 文章列表 -->
    <el-table :data="articles" style="width: 100%">
      <el-table-column
        label="文章标题"
        width="400"
        prop="title"
      ></el-table-column>
      <el-table-column label="一级分类" prop="firstCategoryName"></el-table-column>
      <el-table-column label="二级分类" prop="secondCategoryName"></el-table-column>
      <el-table-column label="三级分类" prop="thirdCategoryName"></el-table-column>
      <el-table-column label="发表时间" prop="createTime"> </el-table-column>
      <el-table-column label="状态" prop="state"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button :icon="Edit" circle plain type="primary" @click="showArticle(row)"></el-button>
          <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <!-- 分页条 -->
    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :page-sizes="[3, 5, 10, 15]"
      layout="jumper, total, sizes, prev, pager, next"
      background
      :total="total"
      @size-change="onSizeChange"
      @current-change="onCurrentChange"
      style="margin-top: 20px; justify-content: flex-end"
    />
    <!-- 抽屉 -->
    <el-drawer v-model="visibleDrawer" :title="title" direction="rtl" size="50%">
      <!-- 添加文章表单 -->
      <el-form :model="articleModel" label-width="100px">
        <el-form-item label="文章标题">
          <el-input
            v-model="articleModel.title"
            placeholder="请输入标题"
          ></el-input>
        </el-form-item>
        <el-form-item label="一级分类：">
        <el-select
          placeholder="请选择"
          v-model="category_first_add"
          style="width: 100px"
          @change="category_second_add='';categoryId=''"
        >
          <el-option
            v-for="c in categorys_firstname"
            :key="c.id"
            :label="c.firstCategoryName"
            :value="c.firstCategoryName"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="二级分类：">
        <el-select
          placeholder="请选择"
          v-model="category_second_add"
          style="width: 100px"
          :disabled = "category_first_add.length === 0"
          @change="category_third_add=''"
        >
          <el-option
            v-for="c in categorys_secondname"
            :key="c.id"
            :label="c.secondCategoryName"
            :value="c.secondCategoryName"
            v-show="c.firstCategoryName == category_first_add"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="三级分类：">
        <el-select
          placeholder="请选择"
          v-model="category_third_add"
          style="width: 100px"
          :disabled = "category_second_add.length === 0 || category_second_add === ''"
        >
          <el-option
            v-for="c in categorys_thirdname"
            :key="c.id"
            :label="c.thirdCategoryName"
            :value="c.thirdCategoryName"
            v-show="c.secondCategoryName === category_second_add"
          >
          </el-option>
        </el-select>
      </el-form-item>
        <el-form-item label="文章内容">
          <div class="editor">
            <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html"></quill-editor>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="findCategoryIDAdd();title == '添加文章'? addArticle('已发布'):updateArticle('已发布');">发布</el-button>
          <el-button type="info" @click="findCategoryIDAdd();title == '添加文章'? addArticle('草稿'):updateArticle('草稿')">草稿</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </el-card>
</template>
<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}
/* 抽屉样式 */
.avatar-uploader {
  :deep() {
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }

    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }

    .el-upload:hover {
      border-color: var(--el-color-primary);
    }

    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      text-align: center;
    }
  }
}
.editor {
  width: 100%;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>