package org.example.spring.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.spring.dto.UserCreateUpdateDto;
import org.example.spring.dto.UserDto;
import org.example.spring.entities.User;
import org.example.spring.mappers.UserMapper;
import org.example.spring.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        if (userRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User with email " + dto.email() + " already exists"
            );
        }

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
                        new EntityNotFoundException(
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
                        new EntityNotFoundException(
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