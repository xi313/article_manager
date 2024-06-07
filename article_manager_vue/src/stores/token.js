//定义store
import {defineStore} from 'pinia'
import { ref } from 'vue';
/**
 * 参数1：名字，唯一性
 * 参数2：函数，内部可以定义状态的所有内容
 * 返回函数
 */

export const useTokenStore = defineStore('token',()=>{
    //定义变量内容
    //1.响应式变量
    const token = ref('');
    //定义一个函数修改token
    const setToken = (newToken) =>{
        token.value = newToken
    }
    //移除toktn
    const removeToken = () =>{
        token.value = ''
    }
    return{
        token,setToken,removeToken
    }
}
,{persist:true}//持久化存储
);
