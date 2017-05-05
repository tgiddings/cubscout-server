package com.robocubs4205.cubscout.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "not found")
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(){

    }
    public ResourceNotFoundException(String reason){
        super(reason);
    }
    public ResourceNotFoundException(Throwable t){
        super(t);
    }
}
