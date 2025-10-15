package com.zapp.client_service.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortDirValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSortDir {
    String message() default "sortDir must be either 'ASC' or 'DESC'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
