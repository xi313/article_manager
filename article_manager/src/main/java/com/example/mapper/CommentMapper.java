package com.example.mapper;


import com.example.pojo.Article;
import com.example.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    //添加评论
    @Insert("insert into comment(content,article_id,create_user,create_time,update_time) " +
            " values(#{content},#{articleId},#{createUser},#{createTime},#{updateTime})")
    void add(Comment comment);

    //分页查询
    List<Article> list(Integer articleId,Integer userId);

    //根据Id查询评论
    @Select("select * from comment where id=#{id}")
    Comment findById(Integer id);

    //更新评论
    @Update("update comment set content=#{content},update_time=#{updateTime} where id = #{id}")
    void update(Comment comment);

    //删除评论
    @Delete("delete from comment where id = #{id}")
    void delete(Integer id);
}
