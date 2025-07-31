package com.zapp.job_service.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class BusinessValidationException extends RuntimeException {
    private final List<String> errors;

    public BusinessValidationException(String message) {
        super(message);
        this.errors = Collections.emptyList();
    }

    public BusinessValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors == null ? Collections.emptyList() : errors;
    }
}
