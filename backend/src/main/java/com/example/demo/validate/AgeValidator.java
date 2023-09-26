package com.example.demo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class AgeValidator implements ConstraintValidator<Age, String> {
    @Override
    public boolean isValid(String stringDate, ConstraintValidatorContext constraintValidatorContext) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate localDate = LocalDate.parse(stringDate, dateTimeFormatter);
            Period period = Period.between(localDate, LocalDate.now());
            return period.getYears() >= 15;
        } catch (Exception exception) {
            return false;
        }

    }
}
