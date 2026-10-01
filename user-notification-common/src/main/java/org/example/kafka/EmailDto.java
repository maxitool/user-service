package org.example.kafka;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailDto(
        @NotBlank(message = "Email can't be blank")
        @Size(min = 1, max = 100, message = "Length of email must be between 1 and 100")
        @Email(message = "Incorrect email format")
        @Schema(
                description = "User email",
                example = "ivan@example.com"
        )
        String email
) {
}
