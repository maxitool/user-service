package org.example.hibernate.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UserCreateUpdateDto(
        @NotBlank(message = "Name can't be null")
        @NotNull(message = "Name can't be null")
        @Size(min = 1, max = 100, message = "Length of name must be between 1 and 100")
        String name,
        @NotBlank(message = "Email can't be null")
        @NotNull(message = "Email can't be null")
        @Size(min = 1, max = 100, message = "Length of email must be between 1 and 100")
        @Email(message = "Incorrect email format")
        String email,
        @NotBlank(message = "Age can't be null")
        @NotNull(message = "Age can't be null")
        @Positive(message = "Age must be greater than 0")
        Integer age) {
}
