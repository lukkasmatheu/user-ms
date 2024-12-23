package com.vm.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class UserMaintenanceException extends RuntimeException {
    public UserMaintenanceException(String message) {
        super(message);
    }
    public UserMaintenanceException(String message,Exception exception){
        super(message,exception);
    }
}

