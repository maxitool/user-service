package org.example.hibernate.mapper;

import org.example.hibernate.dto.UserCreateUpdateDto;
import org.example.hibernate.dto.UserDto;
import org.example.hibernate.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateUpdateDto userCreateUpdateDto) {
        return User.builder()
                .name(userCreateUpdateDto.name())
                .email(userCreateUpdateDto.email())
                .age(userCreateUpdateDto.age())
                .build();
    }

    public UserDto toDto (User user) {
        return UserDto.builder()
                .id(user.getId())
                .age(user.getAge())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

}
