import request from "@/utils/request.js";
import { useTokenStore } from "@/stores/token";

//文章分类列表查询
export const articleCategoryListService = () => {
    const tokenStore = useTokenStore();
    //return request.get('/category',{headers: {'Authorization': tokenStore.token}});
    return request.get('/category');
}
export const articleCategoryAllListService = () => {
    const tokenStore = useTokenStore();
    //return request.get('/category',{headers: {'Authorization': tokenStore.token}});
    return request.get('/category/all');
}
//文章分类添加
export const articleCategoryAddService = (categoryData) => {
    return request.post('/category',categoryData)
}
//文章分类修改
export const articleCategoryUpdateService = (categoryData) => {
    return request.put('/category',categoryData)
}

//文章分类删除
export const articleCategoryDeleteService = (id) => {
    return request.delete('/category?id='+id)
}
//文章列表查询
export const articleListService = (params)=>{
    return request.get('/article',{params:params})
}
//文章添加
export const articleAddService = (articleData)=>{
    return request.post('/article',articleData)
}
//文章修改
export const articleDeleteService = (id)=>{
    return request.delete('/article?id='+id)
}
//文章更新
export const articleUpdateService = (articleData)=>{
    return request.put('/article',articleData)
}
//文章阅读
export const articleReadService = (params)=>{
    return request.get('/article/read',{params:params})
}
