package org.example.hibernate.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BusinessLogicExceptionsAbstract {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}


