package org.example.spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "User information")
public record UserDto(

        @Schema(
                description = "User identifier",
                example = "1"
        )
        Long id,

        @Schema(
                description = "User name",
                example = "Ivan Ivanov"
        )
        String name,

        @Schema(
                description = "User email",
                example = "ivan@example.com"
        )
        String email,

        @Schema(
                description = "User age",
                example = "25"
        )
        Integer age
) {
}