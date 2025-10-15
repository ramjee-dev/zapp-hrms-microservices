package com.zapp.candidate_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Sort;

/**
 * Custom validator to ensure that the sort direction
 * is either "ASC" or "DESC" (case-insensitive).
 */
public class SortDirectionValidator implements ConstraintValidator<SortDirection, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Let @NotBlank handle null or empty cases
        }

        try {
            Sort.Direction.fromString(value.trim());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
