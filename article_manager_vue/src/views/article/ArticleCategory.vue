<script setup>
import { Edit, Delete } from "@element-plus/icons-vue";
import { ref } from "vue";
const categorys = ref([]);

//声明异步函数
import {
  articleCategoryListService,
  articleCategoryAddService,
  articleCategoryUpdateService,
  articleCategoryDeleteService
} from "@/api/article.js";
const articleCategoryList = async () => {
  let result = await articleCategoryListService();
  categorys.value = result.data;
};
articleCategoryList();
//控制添加分类弹窗
const dialogVisible = ref(false);

//添加分类数据模型
const categoryModel = ref({
  firstCategoryName: "",
  secondCategoryName: "",
  thirdCategoryName: ""
});
//添加分类表单校验
const rules = {
  firstCategoryName: [
    { required: true, message: "请输入分类名称", trigger: "blur" },
  ]
};

//调用接口添加表单
import { ElMessage,ElMessageBox } from "element-plus";
const addCategory = async () => {
  let result = await articleCategoryAddService(categoryModel.value);
  ElMessage.success(result.msg ? result.mag : "添加成功");
  //调用获取文章分类函数
  articleCategoryList();
  dialogVisible.value = false;
};
//定义变量控制标题展示
const title = ref("");
//编辑分类弹窗
const showCategory = (row) => {
  dialogVisible.value = true;
  title.value = "编辑分类";
  categoryModel.value.firstCategoryName = row.firstCategoryName;
  categoryModel.value.secondCategoryName = row.secondCategoryName;
  categoryModel.value.thirdCategoryName = row.thirdCategoryName;
  categoryModel.value.id = row.id;
};
const updateCategory = async () => {
  //调用接口
  let result = await articleCategoryUpdateService(categoryModel.value);
  ElMessage.success(result.msg ? result.mag : "修改成功");
  articleCategoryList();
  dialogVisible.value = false;
};
const clearData = () => {
  categoryModel.value.firstCategoryName = "";
  categoryModel.value.secondCategoryName = '';
  categoryModel.value.thirdCategoryName = '';
};

//删除分类
const deleteCategory = (row) => {
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
        let result = await articleCategoryDeleteService(row.id);
      ElMessage({
        type: "success",
        message: "删除成功",
      });
      //刷新列表
      articleCategoryList();
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "用户取消删除",
      });
    });
};
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文章分类</span>
        <div class="extra">
          <el-button
            type="primary"
            @click="
              dialogVisible = true;
              title = '添加分类';
              clearData();
            "
            >添加分类</el-button
          >
        </div>
      </div>
    </template>
    <el-table :data="categorys" style="width: 100%">
      <el-table-column label="序号" width="100" type="index"> </el-table-column>
      <el-table-column label="一级分类名称" prop="firstCategoryName"></el-table-column>
      <el-table-column label="二级分类名称" prop="secondCategoryName"></el-table-column>
      <el-table-column label="三级分类名称" prop="thirdCategoryName"></el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button
            :icon="Edit"
            circle
            plain
            type="primary"
            @click="showCategory(row)"
          ></el-button>
          <el-button
            :icon="Delete"
            circle
            plain
            type="danger"
            @click="deleteCategory(row)"
          ></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <!-- 添加分类弹窗 -->
    <el-dialog v-model="dialogVisible" :title="title" width="30%">
      <el-form
        :model="categoryModel"
        :rules="rules"
        label-width="150px"
        style="padding-right: 30px"
      >
        <el-form-item label="一级分类名称" prop="firstCategoryName">
          <el-input
            v-model="categoryModel.firstCategoryName"
            minlength="1"
            maxlength="10"
          ></el-input>
        </el-form-item>
        <el-form-item label="二级分类名称" prop="secondCategoryName">
          <el-input
            v-model="categoryModel.secondCategoryName"
            minlength="1"
            maxlength="10"
          ></el-input>
        </el-form-item>
        <el-form-item label="三级分类名称" prop="thirdCategoryName">
          <el-input
            v-model="categoryModel.thirdCategoryName"
            minlength="1"
            maxlength="10"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="title == '添加分类' ? addCategory() : updateCategory()"
          >
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
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
</style>