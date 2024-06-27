import {createRouter,createWebHistory} from 'vue-router'
//导入组件
import LoginVue from "@/views/Login.vue"
import LayoutVue from "@/views/Layout.vue"
import ArticleCategoryVue from "@/views/article/ArticleCategory.vue"
import ArticleManageVue from '@/views/article/ArticleManage.vue'
import ArticleReadVue from '@/views/article/ArticleRead.vue'
import UserInfoVue from '@/views/user/UserInfo.vue'
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue'



//定义路由关系
const routes = [
    {path: '/login', component: LoginVue},
    {path: '/', component: LayoutVue,redirect:'/article/read', children: [
        {path:'/article/category',component:ArticleCategoryVue },
        {path:'/article/manage',component:ArticleManageVue},
        {path:'/article/read',component:ArticleReadVue},
        {path:'/user/info',component:UserInfoVue},
        {path:'/user/resetPassword',component:UserResetPasswordVue},
    ]},
]

//创建路由
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

//导出路由
export default router
