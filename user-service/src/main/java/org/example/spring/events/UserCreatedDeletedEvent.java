package org.example.spring.events;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.kafka.Operation;

public record UserCreatedDeletedEvent(
        @Schema(
                description = "Operation performed on the user",
                example = "create",
                allowableValues = {"create", "delete"}
        )
        @NotNull(message = "Operation must not be null")
        Operation operation,
        @Schema(
                description = "Unique user email",
                example = "test@mail.ru"
        )
        @NotBlank(message = "Email can't be blank")
        @Size(min = 1, max = 100, message = "Length of email must be between 1 and 100")
        @Email(message = "Incorrect email format")
        String email
) {
}
