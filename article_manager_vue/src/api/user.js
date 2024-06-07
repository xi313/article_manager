//导入request.js
import request from '@/utils/request.js'

//提供注册接口函数
export const userRegisterService = (registerData)=>{
    const params = new URLSearchParams()
    for(let key in registerData){
        params.append(key, registerData[key]);
    }
    return request.post('/user/register',params);
}

//提供登录接口函数
export const userLoginService = (loginData)=>{
    const params = new URLSearchParams()
    for(let key in loginData){
        params.append(key, loginData[key]);
    }
    return request.post('/user/login',params);
}

//获取个人信息
export const userInfoGetService = ()=>{
    return request.get('/user/userInfo');
}

//修改个人信息
export const userInfoUpdateService = (userInfo)=>{
    return request.put('/user/update',userInfo)
}
//修改密码
export const userPasswordUpdateService = (password)=>{
    return request.patch('/user/updatePwd',password)
}