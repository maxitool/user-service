package org.example.hibernate.dto;

import jakarta.validation.constraints.*;

public record UserCreateUpdateDto(
        @NotNull(message = "Name can't be null")
        @Size(min = 1, max = 100, message = "Length of name must be between 1 and 100")
        String name,
        @NotNull(message = "Email can't be null")
        @Size(min = 1, max = 100, message = "Length of email must be between 1 and 100")
        @Email(message = "Incorrect email format")
        String email,
        @NotNull(message = "Age can't be null")
        @Positive(message = "Age must be greater than 0")
        Integer age) {
}
