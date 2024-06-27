<script setup>
import { Reading, Edit, Delete } from "@element-plus/icons-vue";

import { ref, shallowRef } from "vue";

//文章分类数据模型
const categorys = ref([]);
//文章分类的各级名字
const categorys_firstname = ref([]);
const categorys_secondname = ref([]);
const categorys_thirdname = ref([]);
//用户搜索时选中的分类id
const categoryId = ref([]);
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
//日期
const date = ref("");
//用户搜索时的关键字
const titleKeyword = ref("");
const contentKeyword = ref("");
//文章列表数据模型
const articles = ref([]);
//评论列表数据模型
const comments = ref([]);
//文章分页条数据模型
const pageNum_article = ref(1); //当前页
const total_article = ref(20); //总条数
const pageSize_article = ref(3); //每页条数
const pageSizes_article = ref([3, 5, 10, 15]);
//评论分页条数据模型
const pageNum_comment = ref(1); //当前页
const total_comment = ref(20); //总条数
const pageSize_comment = ref(3); //每页条数
const pageSizes_comment = ref([3, 5, 10, 15]);
//当每页条数发生了变化，调用此函数
const onSizeChange_article = (size) => {
  pageSize_article.value = size;
  articleList();
};
const onSizeChange_comment = (size) => {
  pageSize_comment.value = size;
  commentList();
};
//当前页码发生变化，调用此函数
const onCurrentChange_article = (num) => {
  pageNum_article.value = num;
  articleList();
};
const onCurrentChange_comment = (num) => {
  pageNum_comment.value = num;
  commentList();
};
//回写文章分类
import {
  articleCategoryAllListService,
  articleReadService,
  articleAddService,
  articleDeleteService,
  articleUpdateService,
} from "@/api/article.js";
const articleCategoryList = async () => {
  let result = await articleCategoryAllListService();
  categorys.value = result.data;
  const first = {};
  const second = {};
  const third = {};
  for (let i = 0; i < result.data.length; i++) {
    let category = result.data[i];
    if (!first[category.firstCategoryName]) {
      first[category.firstCategoryName] = {};
    }
    if (!second[category.secondCategoryName]) {
      second[category.secondCategoryName] = {
        firstCategoryName: category.firstCategoryName,
      };
    }
    if (!third[category.thirdCategoryName]) {
      third[category.thirdCategoryName] = {
        secondCategoryName: category.secondCategoryName,
      };
    }
  }
  categorys_firstname.value = Object.keys(first).map((firstname) => ({
    firstCategoryName: firstname, // 使用 firstname 作为 value（如果需要）
  }));
  categorys_secondname.value = Object.keys(second).map((secondname) => ({
    firstCategoryName: second[secondname].firstCategoryName,
    secondCategoryName: secondname, // 使用 firstname 作为 value（如果需要）
  }));
  categorys_thirdname.value = Object.keys(third).map((thirdname) => ({
    secondCategoryName: third[thirdname].secondCategoryName,
    thirdCategoryName: thirdname, // 使用 firstname 作为 value（如果需要）
  }));
};
const findCategoryID = () => {
  categoryId.value = [];
  for (let i = 0; i < categorys.value.length; i++) {
    let category = categorys.value[i];
   /* if (
      category_first.value == category.firstCategoryName &&
      category_second.value == category.secondCategoryName &&
      category_third.value == category.thirdCategoryName
    ) {
      categoryId.value.push(category.id);
    }*/
    if(category_first.value == category.firstCategoryName){
      if(category_second.value == "" || category_second.value == null){
        categoryId.value.push(category.id);
      }else if(category_second.value == category.secondCategoryName){
          if(category_third.value == "" || category_third.value == null){
            categoryId.value.push(category.id);
          }else if(category_third.value == category.thirdCategoryName){
            categoryId.value.push(category.id);
          }else{
          }
      }else{
      }
    }
  }
  if(category_first.value != "" && categoryId.value.length == 0){
    categoryId.value.push(-1);
  }
};
//获取文章列表数据
const articleList = async () => {
  let params = {
    pageNum: pageNum_article.value,
    pageSize: pageSize_article.value,
    categoryId: categoryId.value.length != 0 ? categoryId.value : null,
    date: date.value ? date.value : null,
    titleKeyword: titleKeyword.value ? titleKeyword.value : null,
    contentKeyword: contentKeyword.value ? contentKeyword.value : null,
  };
  let result = await articleReadService(params);
  //渲染视图
  total_article.value = result.data.total;
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

import wangEditor from "@/editor/wangEditor.vue";
import "@wangeditor/editor/dist/css/style.css"; // 引入 css

import { onBeforeUnmount, onMounted } from "vue";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef();
const toolbarConfig = {};
const editorConfig = {
  readOnly: true,
};
// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});
const handleCreated = (editor) => {
  editorRef.value = editor; // 记录 editor 实例，重要！
};

import { Plus } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
//控制抽屉是否显示
const visibleDrawer = ref(false);
//文章数据模型
const articleModel = ref({
  id: "",
  title: "",
  categoryId: "",
  content: "",
});
const comment = ref({
  content: "",
  articleId: "",
  usserId: "",
});
const articleId = ref("");
//编辑文章
const showArticle = (row) => {
  visibleDrawer.value = true;
  articleModel.value.id = row.id;
  articleModel.value.categoryId = row.categoryId;
  articleModel.value.title = row.title;
  articleModel.value.content = row.content;
  articleId.value = row.id;
};
const clearData = () => {
  articleModel.value.categoryId = "";
  articleModel.value.title = "";
  articleModel.value.content = "";
};
const clearSearch = () => {
  categoryId.value = [];
  state.value = "";
  category_first.value = "";
  category_second.value = "";
  category_third.value = "";
  date.value = "";
  titleKeyword.value = "";
  contentKeyword.value = "";
};

import {
  commentListService,
  commentListSelfService,
  commentAddService,
  commentDeleteService,
} from "@/api/comment.js";

const addComment = async () => {
  if (comment.value.content == "") {
    ElMessage.error("评论内容为空");
  } else {
    comment.value.articleId = articleId.value;
    let result = await commentAddService(comment.value);
    ElMessage.success(result.msg ? result.msg : "添加成功");
    comment.value.content = "";
    commentList();
  }
};
const deleteComment = (row) => {
  ElMessageBox.confirm("确认删除该分类吗", "温馨提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      //调用接口
      let result = await commentDeleteService(row.id);
      ElMessage({
        type: "success",
        message: "删除成功",
      });
      //刷新列表
      commentList();
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "用户取消删除",
      });
    });
};
const commentList = async () => {
  let params = {
    pageNum: pageNum_comment.value,
    pageSize: pageSize_comment.value,
    articleId: articleModel.value.id,
  };
  let result = await commentListService(params);
  //渲染视图
  total_comment.value = result.data.total;
  comments.value = result.data.items;
};
const commentListSelf = async () => {
  let params = {
    pageNum: pageNum_comment.value,
    pageSize: pageSize_comment.value,
    articleId: articleModel.value.id,
  };
  let result = await commentListSelfService(params);
  //渲染视图
  total_comment.value = result.data.total;
  comments.value = result.data.items;
};
import { useUserInfoStore } from "@/stores/userInfo.js";
const userInfoStore = useUserInfoStore();
const userInfo = ref({ ...userInfoStore.info });
</script>
<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文章阅读</span>
      </div>
    </template>
    <!-- 搜索表单 -->
    <el-form inline>
      <el-form-item label="文章一级分类：">
        <el-select
          placeholder="请选择"
          v-model="category_first"
          style="width: 100px"
          @change="
            category_second = '';
            category_third = '';
          "
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
          :disabled="category_first.length === 0"
          @change="category_third = ''"
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
          :disabled="category_second.length === 0 || category_second === ''"
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
      <el-form-item label="日期：">
        <el-date-picker
          v-model="date"
          type="date"
          placeholder="选择发布日期"
          :size="size"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="标题关键词：">
        <el-input
          v-model="titleKeyword"
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item label="内容关键词：">
        <el-input
          v-model="contentKeyword"
          placeholder="请输入"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="
            findCategoryID();
            articleList();
          "
          >搜索</el-button
        >
        <el-button @click="clearSearch();articleList()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 文章列表 -->
    <el-table :data="articles" style="width: 100%">
      <el-table-column
        label="文章标题"
        width="400"
        prop="title"
      ></el-table-column>
      <el-table-column
        label="一级分类"
        prop="firstCategoryName"
      ></el-table-column>
      <el-table-column
        label="二级分类"
        prop="secondCategoryName"
      ></el-table-column>
      <el-table-column
        label="三级分类"
        prop="thirdCategoryName"
      ></el-table-column>
      <el-table-column label="发表时间" prop="createTime"> </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button
            :icon="Reading"
            circle
            plain
            type="primary"
            @click="
              showArticle(row);
              commentList();
            "
          ></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <!-- 分页条 -->
    <el-pagination
      v-model:current-page="pageNum_article"
      v-model:page-size="pageSize_article"
      :page-sizes="pageSizes_article"
      layout="jumper, total, sizes, prev, pager, next"
      background
      :total="total_article"
      @size-change="onSizeChange_article"
      @current-change="onCurrentChange_article"
      style="margin-top: 20px; justify-content: flex-end"
    />
    <!-- 抽屉 -->
    <el-drawer
      v-model="visibleDrawer"
      :title="articleModel.title"
      direction="rtl"
      size="80%"
    >
      <template #header="{ titleId, titleClass }">
        <h1 :id="titleId" :class="titleClass">{{ articleModel.title }}</h1>
      </template>
      <Editor
        style="height: 50%; overflow-y: hidden"
        v-model="articleModel.content"
        :defaultConfig="editorConfig"
        :mode="mode"
        @onCreated="handleCreated"
      />
      <div class="extra">
        <el-input
          v-model="comment.content"
          placeholder="请输入评论"
          clearable
          style="width: 50%; margin-top: 20px; justify-content: flex-start"
        />
        <el-button
          style="margin-top: 20px; justify-content: flex-end"
          type="primary"
          @click="addComment()"
          >评论</el-button
        >
        <el-button
          style="margin-top: 20px; justify-content: flex-end"
          @click="commentListSelf()"
          >我的评论</el-button
        >
        <el-button
          style="margin-top: 20px; justify-content: flex-end"
          @click="commentList()"
          >所有评论</el-button
        >
      </div>
      <el-table :data="comments" style="width: 100%; height: 30%">
        <el-table-column
          label="评论"
          width="500"
          prop="content"
        ></el-table-column>
        <el-table-column label="发表时间" prop="createTime"> </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button
              :icon="Delete"
              circle
              plain
              :disabled="row.createUser != userInfo.id"
              type="danger"
              @click="deleteComment(row)"
            ></el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无评论" />
        </template>
      </el-table>
      <el-pagination
        v-model:current-page="pageNum_comment"
        v-model:page-size="pageSize_comment"
        :page-sizes="pageSizes_comment"
        layout="jumper, total, sizes, prev, pager, next"
        background
        small
        :total="total_comment"
        @size-change="onSizeChange_comment"
        @current-change="onCurrentChange_comment"
        style="margin-top: 20px; justify-content: flex-start"
      />
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