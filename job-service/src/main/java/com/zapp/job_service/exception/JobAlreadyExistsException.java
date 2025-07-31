package com.zapp.job_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;


/**
 * Exception thrown when attempting to create or register a job that already exists.
 */
public class JobAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new JobAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message
     */
    public JobAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructs a new JobAlreadyExistsException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public JobAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return getClass().getName() + ": " + getMessage();
    }
}
