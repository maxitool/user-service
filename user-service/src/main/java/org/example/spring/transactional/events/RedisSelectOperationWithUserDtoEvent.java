package org.example.spring.transactional.events;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.example.spring.dto.UserDto;
import org.example.spring.enums.SelectOperation;

public record RedisSelectOperationWithUserDtoEvent(
        @Schema(
                description = "Select operation performed on the user",
                example = "create",
                allowableValues = {"findById", "findByEmail"}
        )
        @NotNull(message = "Select operation must not be null")
        SelectOperation selectOperation,
        @NotNull(message = "UserDto must not be null")
        UserDto userDto
) {
}
