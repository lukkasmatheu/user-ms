package com.vm.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class GetUserException extends RuntimeException {

    public GetUserException(String message,Exception exception){
        super(message,exception);
    }

    public GetUserException(String message){
        super(message);
    }
}
