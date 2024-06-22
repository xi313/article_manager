package com.example.service;

import com.example.pojo.Article;
import com.example.pojo.Comment;
import com.example.pojo.PageBean;

public interface CommentService {
    //添加评论
    void add(Comment comment);

    //查看评论列表
    PageBean<Comment> list(Integer pageNum, Integer pageSize, Integer articleId);

    //查看自己文章列表
    PageBean<Comment> listByUserId(Integer pageNum, Integer pageSize, Integer articleId);

    //根据Id查找评论
    Comment findById(Integer id);

    //更新评论
    void update(Comment comment);

    //删除评论
    void delete(Integer id);
}
