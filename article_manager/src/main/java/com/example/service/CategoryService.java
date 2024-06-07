package com.example.service;

import com.example.pojo.Category;
import com.example.pojo.Result;

import java.util.List;

public interface CategoryService {
    //新增文章分类
    void add(Category category);
    //列表查询
    List<Category> list();
    //根据id查询
    Category findById(Integer id);
    //根据名字查询
    List<Category> findByFirstName(String categoryName);
    //更新分类
    void update(Category category);
    //删除分类
    void delete(Integer id);
}
