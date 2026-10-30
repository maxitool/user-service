package org.example.spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

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
                example = "test@mail.ru"
        )
        String email,

        @Schema(
                description = "User age",
                example = "25"
        )
        Integer age
) {
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        UserDto userDto = (UserDto) object;
        return Objects.equals(id, userDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public @NonNull String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}