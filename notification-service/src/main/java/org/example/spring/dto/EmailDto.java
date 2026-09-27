package org.example.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDto(
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректные данные")
        String email) {

    public static EmailDto of(String email) {
        return new EmailDto(email);
    }
}
