package com.example.service;

import com.example.pojo.Article;
import com.example.pojo.PageBean;

public interface ArticleService {
    //新增文章
    void add(Article article);
    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);
    //根据id查找
    Article findArticleById(Integer id);
    //更新文章
    void update(Article article);
    //删除文章
    void delete(Integer id);
    //分页查询发布的文章
    PageBean<Article> readList(Integer pageNum, Integer pageSize, Integer categoryId);
}
