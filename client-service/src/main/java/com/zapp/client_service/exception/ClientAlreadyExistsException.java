package com.zapp.client_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;


/**
 * Exception thrown when attempting to create or register a client that already exists.
 */
@Getter
public class ClientAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String clientName;

    /**
     * Constructs a new exception with the specified detail message and client name.
     *
     * @param message The detail message.
     * @param clientName The name of the client that caused the duplication error.
     */
    public ClientAlreadyExistsException(String message, String clientName) {
        super(message);
        this.clientName = clientName;
    }

    /**
     * Constructs a new exception with the specified detail message, client name, and cause.
     *
     * @param message The detail message.
     * @param clientName The name of the client that caused the duplication error.
     * @param cause The cause of this exception.
     */
    public ClientAlreadyExistsException(String message, String clientName, Throwable cause) {
        super(message, cause);
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return getClass().getName() + ": " + getMessage() + " [clientName=" + clientName + "]";
    }
}
