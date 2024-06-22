package com.example.service.impl;

import com.example.mapper.CommentMapper;
import com.example.pojo.Article;
import com.example.pojo.Comment;
import com.example.pojo.PageBean;
import com.example.service.CommentService;
import com.example.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void add(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        comment.setCreateUser(id);
        commentMapper.add(comment);
    }

    @Override
    public PageBean<Comment> list(Integer pageNum, Integer pageSize, Integer articleId) {
        //创建PageBean、
        PageBean<Comment> pb = new PageBean<>();
        //开启分页查询PageHelper
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> as = commentMapper.list(articleId,null);
        //Page可以获取PageHelper分页查询后得到的总记录条数和当前数据
        Page<Comment> p = (Page<Comment>) as;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public PageBean<Comment> listByUserId(Integer pageNum, Integer pageSize, Integer articleId) {
        //创建PageBean、
        PageBean<Comment> pb = new PageBean<>();
        //开启分页查询PageHelper
        PageHelper.startPage(pageNum,pageSize);
        //调用mapper
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Comment> as = commentMapper.list(articleId,userId);
        //Page可以获取PageHelper分页查询后得到的总记录条数和当前数据
        Page<Comment> p = (Page<Comment>) as;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public Comment findById(Integer id) {
        return commentMapper.findById(id);
    }

    @Override
    public void update(Comment comment) {
        comment.setUpdateTime(LocalDateTime.now());
        commentMapper.update(comment);
    }

    @Override
    public void delete(Integer id) {
        commentMapper.delete(id);
    }
}
