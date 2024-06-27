<script setup>
import { ref } from 'vue'
import { useUserInfoStore } from '@/stores/userInfo.js';
const userInfoStore = useUserInfoStore()
const userInfo = ref({...userInfoStore.info})
const username = ref("");
const nickname = ref("");
const email = ref("");

const rules = {
    nickname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/,
            message: '昵称必须是2-10位的非空字符串',
            trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
    ]
}


const show = () => {
    username.value = userInfo.value.username
    nickname.value = userInfo.value.nickname
    email.value = userInfo.value.email
}
show()
//修改个人信息
import {userInfoUpdateService} from '@/api/user.js'
import { ElMessage } from 'element-plus';
const updateUserInfo = async ()=>{
    userInfo.value.username = username.value
    userInfo.value.nickname = nickname.value
    userInfo.value.email = email.value 
    let result = await userInfoUpdateService(userInfo.value)
    ElMessage.success(result.message? result.message:'修改成功')
    //更新pinia中的数据
    userInfoStore.setInfo(userInfo.value)
}


</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用户昵称" prop="nickname">
                        <el-input v-model="nickname"></el-input>
                    </el-form-item>
                    <el-form-item label="用户邮箱" prop="email">
                        <el-input v-model="email"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserInfo">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>