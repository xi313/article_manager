package com.example.controller;


import com.example.pojo.*;
import com.example.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public Result add(@RequestBody @Validated(Comment.Add.class) Comment comment){
        commentService.add(comment);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Comment>> list(
            Integer pageNum,
            Integer pageSize,
            Integer articleId
    ){
        PageBean<Comment> pb = commentService.list(pageNum,pageSize,articleId);
        return Result.success(pb);
    }

    @GetMapping("/self")
    public Result<PageBean<Comment>> listById(
            Integer pageNum,
            Integer pageSize,
            Integer articleId
    ){
        PageBean<Comment> pb = commentService.listByUserId(pageNum,pageSize,articleId);
        return Result.success(pb);
    }

    @PutMapping
    public Result update(@RequestBody @Validated(Comment.Update.class)Comment comment){
        Comment c = commentService.findById(comment.getId());
        if(c == null){
            return Result.error("评论不存在");
        }
        commentService.update(comment);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(Integer id){
        Comment c = commentService.findById(id);
        if(c == null){
            return Result.error("评论不存在");
        }
        commentService.delete(id);
        return Result.success();
    }
}
