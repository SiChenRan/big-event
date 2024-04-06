package com.edu.cqut.validation;

import com.edu.cqut.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StateValidation implements ConstraintValidator<State, String> {
    //提供校验规则

    /**
     * ]
     *
     * @param value                      将来要娇艳的数据
     * @param constraintValidatorContext
     * @return 返回false校验不通过，true通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null)
            return false;
        else return value.equals("已发布") || value.equals("草稿");
    }
}
