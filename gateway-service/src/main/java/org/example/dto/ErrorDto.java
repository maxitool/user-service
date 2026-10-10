package org.example.dto;

import jakarta.validation.constraints.NotBlank;

public record ErrorDto(
        @NotBlank(message = "description can't be blank")
        String description,
        @NotBlank(message = "cause can't be blank")
        String cause
) {
}
