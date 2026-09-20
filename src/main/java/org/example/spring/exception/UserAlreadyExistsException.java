package org.example.spring.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BusinessLogicExceptionsAbstract {
    public UserAlreadyExistsException(String email) {
        super(String.format(ErrorMessag.USER_ALREADY_EXISTS, email));
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}


