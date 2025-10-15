package com.zapp.candidate_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedSortByValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedSortBy {
    String message() default "Invalid sortBy value";
    String[] fields(); // allowed fields
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}