package org.example.spring.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessLogicExceptionsAbstract extends RuntimeException {

    protected BusinessLogicExceptionsAbstract(String message) {
        super(message);
    }

    public abstract HttpStatus getStatus();
}
