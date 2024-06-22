package com.example.service.impl;

import com.example.pojo.Category;
import com.example.service.CategoryService;
import com.example.utils.JwtUtil;
import com.example.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@SpringBootTest
class CategoryServiceImplTest {

    @Resource
    CategoryService categoryService;

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
        Category category = new Category();
        category.setFirstCategoryName("Test Category");
        categoryService.add(category);
        System.out.println("add");
        System.out.println(category);
    }

    @Test
    void list() {
        List<Category> categoryList = categoryService.list();
        System.out.println("list");
        for (Category category : categoryList) {
            System.out.println(category);
        }
    }

    @Test
    Category findById() {
        Category category = categoryService.findById(0);
        System.out.println("findById 0 :");
        System.out.println(category);
        return category;
    }

    @Test
    void findByFirstName() {
        List<Category> categoryList = categoryService.findByFirstName("test1");
        System.out.println("findByFirstName test1");
        for (Category category : categoryList) {
            System.out.println(category);
        }
    }

    @Test
    @Transactional
    @Rollback
    void update() {
        Category category = findById();
        category.setFirstCategoryName("Test update");
        categoryService.update(category);
        System.out.println("update");
        System.out.println(category);
    }

    @Test
    @Transactional
    @Rollback
    void delete() {
        categoryService.delete(0);
        System.out.println("delete 0");
    }

    @Test
    void listAll() {
        List<Category> categoryList = categoryService.listAll();
        System.out.println("listAll");
        for (Category category : categoryList) {
            System.out.println(category);
        }
    }
}