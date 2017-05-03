package com.robocubs4205.cubscout.rest.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {
    ResourceNotFoundException(){

    }
    ResourceNotFoundException(String reason){
        super(reason);
    }
}
