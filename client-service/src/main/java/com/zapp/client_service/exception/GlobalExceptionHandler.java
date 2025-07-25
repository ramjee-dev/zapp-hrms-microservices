package com.zapp.client_service.exception;

import com.zapp.client_service.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleClientNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Client not found: clientId='{}', path='{}'", ex.getFieldValue(), request.getRequestURI());
        return buildErrorResponse(
                request.getRequestURI(),
                HttpStatus.NOT_FOUND,
                "Not Found",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleClientAlreadyExists(ClientAlreadyExistsException ex, HttpServletRequest request) {
        log.warn("Client already exists: clientName='{}', path='{}'", ex.getClientName(), request.getRequestURI());
        return buildErrorResponse(
                request.getRequestURI(),
                HttpStatus.CONFLICT,
                "Conflict",
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessValidation(BusinessValidationException ex, HttpServletRequest request) {
        log.warn("Business validation failed: field='{}', rejectedValue='{}', path='{}'",
                ex.getField(), ex.getRejectedValue(), request.getRequestURI());
        Map<String, Object> details = new HashMap<>();
        details.put("field", ex.getField());
        details.put("rejectedValue", ex.getRejectedValue());
        return buildErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                ex.getMessage(),
                details
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn("Validation failed for request to path='{}'", request.getRequestURI());

        Map<String, String> errors = new LinkedHashMap<>();
        for (var error : ex.getBindingResult().getAllErrors()) {
            String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        return buildErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("Constraint violation for request to path='{}'", request.getRequestURI());

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(field, message);
        });
        return buildErrorResponse(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred for request to path='{}'", request.getRequestURI(), ex);
        return buildErrorResponse(
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                ex.getMessage(),
                null
        );
    }

    // Utility method to reduce duplication
    private ResponseEntity<ErrorResponseDto> buildErrorResponse(
            String path, HttpStatus status, String error, String message, Object details) {
        return ResponseEntity.status(status).body(
                ErrorResponseDto.builder()
                        .path(path)
                        .status(status.value())
                        .error(error)
                        .message(message)
                        .timestamp(LocalDateTime.now())
                        .details(details)
                        .build()
        );
    }
}