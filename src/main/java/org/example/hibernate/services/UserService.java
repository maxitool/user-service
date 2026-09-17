package org.example.hibernate.services;

import org.example.hibernate.dao.UserRepository;
import org.example.hibernate.dto.UserCreateUpdateDto;
import org.example.hibernate.dto.UserDto;
import org.example.hibernate.entities.User;
import org.example.hibernate.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserCreateUpdateDto dto) {
        User user = userMapper.toEntity(dto);
        User saved = userRepository.save(user);

        return userMapper.toDto(saved);
    }

    public UserDto updateUser(Long id, UserCreateUpdateDto dto) {
        User user = getUserOrThrow(id);

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setAge(dto.age());

        User saved = userRepository.save(user);

        return userMapper.toDto(saved);
    }

    public User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User with id " + id + " not found"
                        ));
    }

    public UserDto findById(Long id) {
        User user = getUserOrThrow(id);

        return userMapper.toDto(user);
    }

    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public void deleteById(Long id) {
        User user = getUserOrThrow(id);

        userRepository.delete(user);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User with email " + email + " not found"
                        ));

        return userMapper.toDto(user);
    }

    public List<UserDto> findByName(String name) {
        return userRepository.findByName(name)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public List<UserDto> findByAge(Integer age) {
        return userRepository.findByAge(age)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
}