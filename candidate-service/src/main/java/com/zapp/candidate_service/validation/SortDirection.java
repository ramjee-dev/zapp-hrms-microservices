package com.zapp.candidate_service.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortDirectionValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SortDirection {

    String message() default "Invalid sort direction. Allowed values are 'ASC' or 'DESC'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
