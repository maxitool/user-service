package org.example.spring.services;

import org.example.spring.dto.UserCreateUpdateDto;
import org.example.spring.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserCreateUpdateDto dto);

    UserDto updateUser(Long id, UserCreateUpdateDto dto);

    UserDto findById(Long id);

    List<UserDto> findAll();

    void deleteById(Long id);

    UserDto findByEmail(String email);

    List<UserDto> findByName(String name);

    List<UserDto> findByAge(Integer age);
}
