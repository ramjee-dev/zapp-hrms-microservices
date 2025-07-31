package com.zapp.candidate_service.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String BASE_PROBLEM_URI = "https://api.zapp-hrms.com/problems/";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCandidateNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.warn("Candidate not found: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Candidate Not Found");
        problemDetail.setType(URI.create(BASE_PROBLEM_URI + "candidate-not-found"));
        enrichMetadata(problemDetail, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ProblemDetail> handleCandidateBusinessValidation(BusinessValidationException ex, WebRequest request) {
        log.warn("Candidate business validation failed: {}", ex.getErrors());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Candidate Validation Failed");
        problemDetail.setType(URI.create(BASE_PROBLEM_URI + "candidate-validation-failed"));
        enrichMetadata(problemDetail, request);
        if (ex.getErrors() != null && !ex.getErrors().isEmpty()) {
            problemDetail.setProperty("validationErrors", ex.getErrors());
        }
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        log.warn("Request body validation failed: {} errors", ex.getBindingResult().getErrorCount());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Request Validation Failed");
        problemDetail.setType(URI.create(BASE_PROBLEM_URI + "request-validation-failed"));
        enrichMetadata(problemDetail, request);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = (error instanceof FieldError fe) ? fe.getField() : error.getObjectName();
            errors.put(fieldName, error.getDefaultMessage());
        });
        problemDetail.setProperty("fieldErrors", errors);
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationExceptions(ConstraintViolationException ex, WebRequest request) {
        log.warn("Constraint violation: {}", ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Constraint violation");
        problemDetail.setTitle("Request Constraint Violation");
        problemDetail.setType(URI.create(BASE_PROBLEM_URI + "constraint-violation"));
        enrichMetadata(problemDetail, request);
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldPath = violation.getPropertyPath().toString();
            errors.put(fieldPath, violation.getMessage());
        });
        problemDetail.setProperty("fieldErrors", errors);
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unhandled exception: ", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create(BASE_PROBLEM_URI + "internal-server-error"));
        enrichMetadata(problemDetail, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    /** Uniform enrichment of problem details for traceability, timestamp, and request origin */
    private void enrichMetadata(ProblemDetail pd, WebRequest request) {
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("path", request.getDescription(false));
        // Optionally, add traceId if your observability stack provides one:
        // pd.setProperty("traceId", MDC.get("traceId"));
    }
}