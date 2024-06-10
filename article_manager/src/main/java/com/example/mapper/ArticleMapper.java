package com.example.mapper;

import com.example.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //新增文章
    @Insert("insert into article(title,content,state,category_id,create_user,create_time,update_time) " +
            " values(#{title},#{content},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime})")
    void add(Article article);

    //分页查询（userID下的文章）
    List<Article> list(Integer categoryId, String state, Integer userId);

    //根据id查找
    @Select("Select * from article where id=#{id}")
    Article findArticleById(Integer id);

    //更新
    @Update("update article set title=#{title},content=#{content},state=#{state},category_id=#{categoryId} where id = #{id}")
    void update(Article article);

    //删除
    @Delete("delete from article where id=#{id}")
    void delete(Integer id);

    //分页查询已发布文章（共享）
    List<Article> readList(Integer categoryId);
}
