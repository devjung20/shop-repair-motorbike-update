package com.example.demo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenderValidator implements ConstraintValidator<Gender,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(com.example.demo.entity.enums.Gender.values()).anyMatch(gender -> gender.name().equalsIgnoreCase(s));
    }
}
