package org.example.spring.transactional.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.spring.dto.UserDto;

import java.util.List;

public record RedisFindByNameEvent(
        @NotBlank(message = "Name can't be blank")
        String name,
        @NotNull(message = "UserDtoList can't be null")
        List<UserDto> userDtoList
) {
}
