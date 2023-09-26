package com.example.demo.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthorValidator.class)
@Documented
public @interface Author {

    String message() default "Tác giả không được phép.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
