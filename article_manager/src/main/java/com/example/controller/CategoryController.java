package com.example.controller;

import com.example.pojo.Category;
import com.example.pojo.Result;
import com.example.service.CategoryService;
import com.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody @Validated(Category.Add.class) Category category){
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        List<Category> cs = categoryService.findByFirstName(category.getFirstCategoryName());
        cs.removeIf(c -> !c.getCreateUser().equals(id));
        String secondName = category.getSecondCategoryName();
        String thirdName = category.getThirdCategoryName();
        //移除不同用户的同名分类
        if(thirdName != null && !thirdName.equals("")){
            if(secondName == null || secondName.equals("")){
                return Result.error("二级分类为空，但三级分类存在");
            }
        }
        for (Category c:cs) {
            if(c.getSecondCategoryName().equals(secondName) && c.getThirdCategoryName().equals(thirdName)){
                return Result.error("分类存在");
            }
        }
        if (secondName == null) {
            category.setSecondCategoryName("");
        }else{
            if(thirdName == null){
                category.setThirdCategoryName("");
            }
        }
        categoryService.add(category);
        return Result.success();
    }

    @GetMapping
    public Result<List<Category>> list(){
        List<Category> cs = categoryService.list();
        return Result.success(cs);
    }

    @GetMapping("/all")
    public Result<List<Category>> listAll(){
        List<Category> cs = categoryService.listAll();
        return Result.success(cs);
    }

    @GetMapping("/detail")
    public Result detail(Integer id){
        Category c = categoryService.findById(id);
        if(c == null) return Result.error("id 不存在");
        return Result.success(c);
    }

    @PutMapping
    public Result update(@RequestBody @Validated(Category.Update.class) Category category){
        String secondName = category.getSecondCategoryName();
        String thirdName = category.getThirdCategoryName();
        if((secondName == null || secondName.equals("")) && (thirdName != null && !thirdName.equals(""))){
            return Result.error("二级分类为空，但三级分类存在");
        }
        Category c = categoryService.findById(category.getId());
        if(c != null){
            category.setUpdateTime(LocalDateTime.now());
            categoryService.update(category);
            return Result.success();
        }else {
            return Result.error("分类不存在");
        }
    }

    @DeleteMapping
    public Result delete(Integer id){
        Category c = categoryService.findById(id);
        if(c == null) return Result.error("id 不存在");
        categoryService.delete(id);
        return Result.success();
    }
}
