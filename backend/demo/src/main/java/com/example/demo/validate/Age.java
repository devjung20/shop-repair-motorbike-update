package com.example.demo.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeValidator.class)
@Documented
public @interface Age {
    String message() default "Độ tuổi tối thiểu là 15";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
