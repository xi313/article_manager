package com.example.service.impl;

import com.example.pojo.Article;
import com.example.pojo.Comment;
import com.example.pojo.PageBean;
import com.example.service.CommentService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceImplTest {
    @Resource
    CommentService commentService;

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
        Comment comment = new Comment();
        comment.setContent("test comment");
        comment.setArticleId(0);
        commentService.add(comment);
        System.out.println("add");
        System.out.println(comment);
    }

    @Test
    void list() {
        Integer pageNum = 1;
        Integer pageSize = 10;
        PageBean<Comment> pb = commentService.list(pageNum, pageSize, 0);
        List<Comment> items = pb.getItems();
        System.out.println("list");
        for (Comment comment: items)
            System.out.println(comment);
    }

    @Test
    void listByUserId() {
        Integer pageNum = 1;
        Integer pageSize = 10;
        PageBean<Comment> pb = commentService.listByUserId(pageNum, pageSize, 0);
        List<Comment> items = pb.getItems();
        System.out.println("listByUserId");
        for (Comment comment: items)
            System.out.println(comment);
    }

    @Test
    Comment findById() {
        Comment comment = commentService.findById(0);
        System.out.println("findById");
        System.out.println(comment);
        return comment;
    }

    @Test
    @Transactional
    @Rollback
    void update() {
        Comment comment = findById();
        comment.setContent("test update");
        commentService.update(comment);
        System.out.println("update");
        System.out.println(comment);
    }

    @Test
    @Transactional
    @Rollback
    void delete() {
        commentService.delete(0);
        System.out.println("delete");
    }
}