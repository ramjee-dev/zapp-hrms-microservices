package com.zapp.client_service.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SortByValidator implements ConstraintValidator<ValidSortBy, String> {

    private Set<String> allowedFields;

    @Override
    public void initialize(ValidSortBy constraintAnnotation) {
        allowedFields = new HashSet<>(Arrays.asList(constraintAnnotation.allowed()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return false; // @NotBlank will also catch this
        }

        return allowedFields.contains(value);
    }
}
