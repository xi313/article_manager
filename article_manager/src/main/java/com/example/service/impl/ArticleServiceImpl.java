package com.example.service.impl;

import com.example.mapper.ArticleMapper;
import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.service.ArticleService;
import com.example.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
public class ArticleServiceImpl implements ArticleService {
    
    @Autowired
    private ArticleMapper articleMapper;
    
    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        article.setCreateUser(id);
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //创建PageBean、
        PageBean<Article> pb = new PageBean<>();
        //开启分页查询PageHelper
        PageHelper.startPage(pageNum,pageSize);
        //调用mapper
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Article> as = articleMapper.list(categoryId,state,userId);
        //Page可以获取PageHelper分页查询后得到的总记录条数和当前数据
        Page<Article> p = (Page<Article>) as;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public Article findArticleById(Integer id) {
        return articleMapper.findArticleById(id);
    }

    @Override
    public void update(Article article) {
        articleMapper.update(article);
    }

    @Override
    public void delete(Integer id) {
        articleMapper.delete(id);
    }
}
