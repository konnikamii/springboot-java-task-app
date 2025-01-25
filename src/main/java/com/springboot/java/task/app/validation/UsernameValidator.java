package com.springboot.java.task.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Please provide a username!")
                    .addConstraintViolation();
            return false;
        }
        if (username.length() < 5) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username must be longer than 5 characters!")
                    .addConstraintViolation();
            return false;
        }
        if (username.length() > 30) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username must be shorter than 30 characters!")
                    .addConstraintViolation();
            return false;
        }
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username should only contain letters and numbers!")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}