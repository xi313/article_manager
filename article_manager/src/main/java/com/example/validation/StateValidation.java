package com.example.validation;

import com.example.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateValidation implements ConstraintValidator<State,String> {

    /**
     * @param value 校验的数据
     * @param constraintValidatorContext
     * @return 返回false不通过，true通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(value == null) return false;
        return value.equals("已发布") || value.equals("草稿");
    }
}
