package org.example.spring.services;

import org.example.spring.dto.UserDto;

import java.util.List;

public interface RedisUserService {
    void cacheFindById(UserDto userDto);

    void cacheFindByEmail(UserDto userDto);

    void cacheFindAll(List<UserDto> userDtoList);

    void cacheFindByName(String name, List<UserDto> userDtoList);

    void cacheFindByAge(Integer age, List<UserDto> userDtoList);

    UserDto getCachedFindById(Long id);

    UserDto getCachedFindByEmail(String email);

    List<UserDto> getCachedFindAll();

    List<UserDto> getCachedFindByName(String name);

    List<UserDto> getCachedFindByAge(Integer age);

    void deleteCachedFindAll();

    void deleteCachedFindByName(String name);

    void deleteCachedFindByAge(Integer age);

    void deleteCachedUserDto(UserDto userDto);
}
