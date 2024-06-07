package com.example.mapper;


import com.example.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {


    //新增
    @Insert("insert into category(first_category_name,second_category_name,third_category_name,create_user,create_time,update_time)" +
            " values(#{firstCategoryName},#{secondCategoryName},#{thirdCategoryName},#{createUser},#{createTime},#{updateTime})")
    void add(Category category);

    //查询所有
    @Select("select * from category where create_user = #{id}")
    List<Category> list(Integer id);

    //根据id查询详细信息
    @Select("select * from category where id = #{id}")
    Category findById(Integer id);

    //根据id查询详细信息
    @Select("select * from category where first_category_name=#{categoryName} ")
    List<Category> findByFirstName(String categoryName);

    //更新
    @Update("update category set first_category_name=#{firstCategoryName},second_category_name=#{secondCategoryName},third_category_name=#{thirdCategoryName},update_time=#{updateTime} where id = #{id}")
    void update(Category category);

    //删除
    @Delete("delete from category where id = #{id}")
    void delete(Integer id);
}
