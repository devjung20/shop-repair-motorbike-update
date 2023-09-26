package com.example.demo.validate;

import com.example.demo.entity.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AuthorValidator implements ConstraintValidator<Author,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(Role.values()).anyMatch(role -> role.name().equalsIgnoreCase(s));
    }
}
