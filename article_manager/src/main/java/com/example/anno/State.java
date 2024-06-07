package com.example.anno;


import com.example.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;

import java.lang.annotation.*;


@Documented //元注解
@Constraint(validatedBy = {StateValidation.class}) //提供校验规则的类
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface State {
    //校验失败后的提示信息
    String message() default "参数值只能为已发布或草稿";
    //指定分组
    Class<?>[] groups() default {};
    //负载，获取到State注解的附加信息
    Class<? extends Payload>[] payload() default {};
}
