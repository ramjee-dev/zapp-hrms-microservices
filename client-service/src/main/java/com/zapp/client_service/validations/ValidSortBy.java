package com.zapp.client_service.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortByValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSortBy {
    String message() default "Invalid sortBy field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] allowed();
}