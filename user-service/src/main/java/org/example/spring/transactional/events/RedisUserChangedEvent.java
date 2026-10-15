package org.example.spring.transactional.events;

import jakarta.validation.constraints.NotNull;
import org.example.spring.dto.UserDto;

public record RedisUserChangedEvent(
        @NotNull(message = "UserDto must not be null")
        UserDto userDto
) {
}
