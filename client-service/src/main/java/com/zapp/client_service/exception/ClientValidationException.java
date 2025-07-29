package com.zapp.client_service.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ClientValidationException extends RuntimeException {
    private final List<String> errors;

    public ClientValidationException(String message) {
        super(message);
        this.errors = null;
    }

    public ClientValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
}