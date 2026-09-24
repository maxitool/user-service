package org.example;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Objects;

public record SendToEmailCreateDeleteDto(
        @Schema(
                description = "Database operation",
                allowableValues = {"create", "delete"}
        )
        @NotNull
        @EnumSource(value = Operation.class, names = {"CREATE", "DELETE"})
        Operation operation,
        @NotBlank(message = "Email can't be blank")
        @Size(min = 1, max = 100, message = "Length of email must be between 1 and 100")
        @Email(message = "Incorrect email format")
        String email
) {
    @Override
    public int hashCode() {
        return Objects.hash(operation, email);
    }
}
