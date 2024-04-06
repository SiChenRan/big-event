package com.edu.cqut.anno;

import com.edu.cqut.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StateValidation.class)
public @interface State {
    String message() default "state参数的值只能是已发布或者草稿";

    //指定分组
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
