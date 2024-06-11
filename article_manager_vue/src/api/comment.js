import request from "@/utils/request.js";
import { useTokenStore } from "@/stores/token";

//评论列表
export const commentListService = (params)=>{
    return request.get('/comment',{params:params})
}
//添加评论
export const commentAddService = (commentData)=>{
    return request.post('/comment',commentData)
}
//删除评论
export const commentDeleteService = (id)=>{
    return request.delete('/comment?id='+id)
}