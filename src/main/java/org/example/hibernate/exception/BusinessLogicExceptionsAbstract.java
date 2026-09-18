package org.example.hibernate.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessLogicExceptionsAbstract extends RuntimeException{

    public BusinessLogicExceptionsAbstract(String message) {
        super(message);
    }
    public abstract HttpStatus getStatus();
}
