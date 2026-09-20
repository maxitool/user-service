package org.example.spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Data for creating or updating a user")
public record UserCreateUpdateDto(
        @Schema(
                description = "User name",
                example = "Ivan Ivanov"
        )
        @NotBlank(message = "Name can't be null")
        @NotNull(message = "Name can't be null")
        @Size(min = 1, max = 100, message = "Length of name must be between 1 and 100")
        String name,
        @Schema(
                description = "Unique user email",
                example = "ivan@example.com"
        )
        @NotBlank(message = "Email can't be null")
        @NotNull(message = "Email can't be null")
        @Size(min = 1, max = 100, message = "Length of email must be between 1 and 100")
        @Email(message = "Incorrect email format")
        String email,
        @Schema(
                description = "User age",
                example = "25",
                minimum = "1"
        )
        @NotNull(message = "Age can't be null")
        @Positive(message = "Age must be greater than 0")
        Integer age) {
}
