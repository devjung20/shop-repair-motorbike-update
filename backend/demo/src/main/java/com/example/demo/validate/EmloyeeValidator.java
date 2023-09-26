package com.example.demo.validate;

import com.example.demo.entity.enums.TypeEmployee;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EmloyeeValidator implements ConstraintValidator<EmployeeType,String>{
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(TypeEmployee.values()).anyMatch(typeEmployee -> typeEmployee.name().equalsIgnoreCase(s));
    }
}
