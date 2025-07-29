package com.zapp.job_service.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class JobValidationException extends RuntimeException{

    private final List<String> errors;

    public JobValidationException(String message) {
        super(message);
        this.errors = null;
    }

    public JobValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
}
