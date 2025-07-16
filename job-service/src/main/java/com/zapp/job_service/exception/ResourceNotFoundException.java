package com.zapp.job_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName,String feildName , String fieldValue) {
        super(String.format("%s not found with given input data %s : '%s'",resourceName,feildName,fieldValue));
    }
}

