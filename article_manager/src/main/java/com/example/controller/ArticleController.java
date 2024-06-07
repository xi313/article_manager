package com.example.controller;


import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import com.example.service.ArticleService;
import com.example.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result add(@RequestBody @Validated Article article){
        articleService.add(article);
        return Result.success();
    }


    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state
    ){
        PageBean<Article> pb = articleService.list(pageNum,pageSize,categoryId,state);
        return Result.success(pb);
    }

    @GetMapping("/detail")
    public Result findArticleById(Integer id){
        Article article = articleService.findArticleById(id);
        if(article == null) return Result.error("id不存在");
        return Result.success(article);
    }

    @PutMapping
    public Result update(@RequestBody @Validated Article article){
        Article a = articleService.findArticleById(article.getId());
        if(a == null) return Result.error("文章不存在");
        articleService.update(article);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(Integer id){
        Article a = articleService.findArticleById(id);
        if(a == null) return Result.error("文章不存在");
        articleService.delete(id);
        return Result.success();
    }
}
