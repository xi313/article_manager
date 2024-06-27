package com.example.controller;


import com.example.pojo.Article;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import com.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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


    @PostMapping("/list")
    public Result<PageBean<Article>> list(
//            Integer pageNum,
//            Integer pageSize,
//            @RequestParam(required = false) Integer categoryId,
//            @RequestParam(required = false) String state,
//            @RequestParam(required = false) String date,
//            @RequestParam(required = false) String titleKeyword,
//            @RequestParam(required = false) String contentKeyword
            @RequestBody Map<String,Object> params
    ){
//        Integer[] category_id = null;
//        if(categoryId != null) category_id = new Integer[]{categoryId};
        Integer pageNum = (Integer)params.get("pageNum");
        Integer pageSize = (Integer)params.get("pageSize");
        Object s = params.get("categoryId");
        String state = (String) params.get("state");
        String date =params.get("date") != null ? (String) params.get("date") : null;
        String titleKeyword =params.get("titleKeyword") != null? (String) params.get("titleKeyword") : null;
        String contentKeyword =params.get("contentKeyword") != null? (String) params.get("contentKeyword") : null;
        Integer[] categoryId = null;
        if(s != null){
            ArrayList<Integer> arrayList = new ArrayList<>();
            String category_str = s.toString();
            category_str = category_str.substring(1,category_str.length()-1);
            String[] category_list = category_str.split(",");
            for (String id :
                    category_list) {
                id = id.trim();
                arrayList.add(Integer.parseInt(id));
            }
            categoryId = arrayList.toArray(new Integer[0]);
        }
        PageBean<Article> pb = articleService.list(pageNum,pageSize,categoryId,state,date,titleKeyword,contentKeyword);
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

    @PostMapping("/read")
    public Result findArticleOnlyRead(
            @RequestBody Map<String,Object> params
    ){
        Integer pageNum = (Integer)params.get("pageNum");
        Integer pageSize = (Integer)params.get("pageSize");
        Object s = params.get("categoryId");
        String date =params.get("date") != null ? (String) params.get("date") : null;
        String titleKeyword =params.get("titleKeyword") != null? (String) params.get("titleKeyword") : null;
        String contentKeyword =params.get("contentKeyword") != null? (String) params.get("contentKeyword") : null;
        Integer[] categoryId = null;
        if(s != null){
            ArrayList<Integer> arrayList = new ArrayList<>();
            String category_str = s.toString();
            category_str = category_str.substring(1,category_str.length()-1);
            String[] category_list = category_str.split(",");
            for (String id :
                    category_list) {
                id = id.trim();
                arrayList.add(Integer.parseInt(id));
            }
            categoryId = arrayList.toArray(new Integer[0]);
        }
        PageBean<Article> pb = articleService.readList(pageNum,pageSize,categoryId,date,titleKeyword,contentKeyword);
        return Result.success(pb);
    }
}
