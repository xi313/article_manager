<script setup>
import { ref } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserInfoStore } from '@/stores/userInfo.js';
const userInfoStore = useUserInfoStore()
const Passwords = ref({
    old_pwd: '',
    new_pwd: '',
    re_pwd: ''
})
//定义表单检验规则
//校验密码的函数
const checkRePassword = (rule,value,callback) => {
    if(value===''){
        callback(new Error('请再次确认密码'))
    }else if(value !== Passwords.value.new_pwd){
        callback(new Error('请确保两次输入一致'))
    }else{
        callback()
    }
}
const rules = {
    old_pwd:[
        {required:true,message:'请输入当前密码',trigger:'blur'},
        {min:5,max:16,message:'请输入5-16位非空字符',trigger:'blur'}
    ],
    new_pwd:[
        {min:5,max:16,message:'请输入5-16位非空字符',trigger:'blur'}
    ],
    re_pwd:[
        {validator:checkRePassword,trigger:'blur'}
    ]
}

//修改个人信息
import {userPasswordUpdateService} from '@/api/user.js'
import { ElMessage,ElMessageBox } from 'element-plus';
import {useRouter} from 'vue-router';
const router = useRouter();
import { useTokenStore } from '@/stores/token.js';
const tokenStore = useTokenStore();
const updateUserPassword = async ()=>{
    ElMessageBox.confirm(
            '你确认修改密码吗？',
            '温馨提示',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )
            .then(async () => {
                let result = await userPasswordUpdateService(Passwords.value)
                ElMessage.success(result.message? result.message:'修改成功')
                userInfoStore.removeInfo()
                tokenStore.removeToken()
                router.push('/login')
            })
            .catch(() => {
                //用户点击了取消
                ElMessage({
                    type: 'info',
                    message: '用户退出',
                })
            })
}


</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>密码修改</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="Passwords" :rules="rules" label-width="150px" size="large">
                    <el-form-item label="输入当前密码" prop="old_pwd">
                        <el-input :prefix-icon="Lock" v-model="Passwords.old_pwd"  show-password></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd">
                        <el-input :prefix-icon="Lock" v-model="Passwords.new_pwd" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="再次输入" prop="re_pwd">
                        <el-input :prefix-icon="Lock" v-model="Passwords.re_pwd" show-password ></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserPassword">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>