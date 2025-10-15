package com.zapp.candidate_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tomcat.util.bcel.Const;

import java.util.Arrays;

public class AllowedSortByValidator implements ConstraintValidator<AllowedSortBy, String> {

    private String[] allowedFields;

    @Override
    public void initialize(AllowedSortBy constraintAnnotation) {
        this.allowedFields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Let @NotBlank handle null/empty
        }

        return Arrays.stream(allowedFields)
                .anyMatch(allowed -> allowed.equalsIgnoreCase(value.trim()));
    }
}
