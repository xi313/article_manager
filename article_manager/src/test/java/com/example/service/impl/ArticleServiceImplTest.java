package com.example.service.impl;

import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.service.ArticleService;
import com.example.utils.JwtUtil;
import com.example.utils.ThreadLocalUtil;
import io.lettuce.core.ScriptOutputType;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceImplTest {

    @Resource
    ArticleService articleService;

    @BeforeAll
    public static void init(){
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjbGFpbXMiOnsiaWQiOjAsInVzZXJuYW1lIjoidGVzdF91c2VyIn0sImV4cCI6MTcxOTExMTQ5N30.EEPalbshEoIWjhAy6YrrS1STo_Zgb6XW8FM8WFjC-6U";
        Map<String,Object> claims = JwtUtil.parseToken(token);
        ThreadLocalUtil.set(claims);
    }

    @Test
    @Transactional
    @Rollback
    void add() {
        Article article = new Article();
        article.setTitle("test");
        article.setContent("test");
        article.setState("已发布");
        article.setCategoryId(0);
        articleService.add(article);
        System.out.println("add");
        System.out.println(article);
    }

    @Test
    void list() {
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer[] categoryId = {0};
        PageBean<Article> pb = articleService.list(pageNum,pageSize,categoryId,
                null,null,null,null);
        List<Article> items = pb.getItems();
        System.out.println("list");
        for (Article article: items)
            System.out.println(article);
    }

    @Test
    Article findArticleById() {
        Article article = articleService.findArticleById(0);
        System.out.println("findArticleById 0");
        System.out.println(article);
        return article;
    }

    @Test
    @Transactional
    @Rollback
    void update() {
        Article article = findArticleById();
        article.setContent("update test");
        articleService.update(article);
        System.out.println("update");
        System.out.println(article);
    }

    @Test
    void readList() {
        Integer pageNum = 1;
        Integer pageSize = 10;
        Integer[] categoryId = {0};
        PageBean<Article> pb = articleService.readList(pageNum,pageSize,categoryId,
                null,null,null);
        List<Article> items = pb.getItems();
        System.out.println("readList");
        for (Article article: items)
            System.out.println(article);
    }

    @Test
    @Transactional
    @Rollback
    void delete() {
        articleService.delete(0);
        System.out.println("delete 0");
    }
}