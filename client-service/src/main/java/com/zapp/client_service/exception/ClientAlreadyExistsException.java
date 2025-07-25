package com.zapp.client_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
@Getter
public class ClientAlreadyExistsException extends RuntimeException {
    private final String clientName;

    public ClientAlreadyExistsException(String message, String clientName) {
        super(message);
        this.clientName = clientName;
    }

    public ClientAlreadyExistsException(String message, String clientName, Throwable cause) {
        super(message, cause);
        this.clientName = clientName;
    }
}
