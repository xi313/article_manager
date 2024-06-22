package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id;//主键ID
    @NotEmpty
    private String content;//内容
    @NotNull(groups = Comment.Add.class)
    private Integer articleId;//文章id
    private Integer createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间

    //没有指定的校验项分组属于Default分组
    //分组之间可以继承，继承者拥有被继承者所有校验项
    public interface Add{

    }
    public interface Update{

    }
}
