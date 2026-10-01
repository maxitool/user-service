package org.example.spring.exception;

import java.time.LocalDateTime;

public record ErrorDto(
        String status,
        String message,
        LocalDateTime timestamp) {

    public static ErrorDto of(String code, String message) {
        return new ErrorDto(code, message, LocalDateTime.now());
    }

}
