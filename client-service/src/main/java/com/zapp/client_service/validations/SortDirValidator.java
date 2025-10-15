package com.zapp.client_service.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SortDirValidator implements ConstraintValidator<ValidSortDir, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false; // Let your record constructor apply default instead, or reject
        }
        String normalized = value.trim().toUpperCase();
        return "ASC".equals(normalized) || "DESC".equals(normalized);
    }
}