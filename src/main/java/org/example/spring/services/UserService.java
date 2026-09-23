package org.example.spring.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.spring.dto.user_service.UserCreateUpdateDto;
import org.example.spring.dto.user_service.UserDto;
import org.example.spring.entities.User;
import org.example.spring.exception.ErrorMessage;
import org.example.spring.exception.UserAlreadyExistsException;
import org.example.spring.mappers.UserMapper;
import org.example.spring.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        if (userRepository == null) {
            log.error("userRepository is null");
        }
        this.userRepository = userRepository;
        if (userMapper == null) {
            log.error("userMapper is null");
        }
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserCreateUpdateDto dto) {

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            log.info("{} email already exist.", dto.email());
            throw new UserAlreadyExistsException(dto.email());
        }

        User user = userMapper.toEntity(dto);
        log.debug("UserCreateUpdateDto {} to User entity {} was successful.", dto, user);

        User saved = userRepository.save(user);
        log.info("User {} was created.", user);

        UserDto result = userMapper.toDto(saved);
        log.debug("User {} to UserDto entity {} was successful.", saved, result);

        return result;
    }

    public UserDto updateUser(Long id, UserCreateUpdateDto dto) {
        User user = getUserOrThrow(id);

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setAge(dto.age());
        log.debug("All parameters were successfully set.");

        User saved = userRepository.save(user);
        log.info("User {} was updated.", user);

        UserDto result = userMapper.toDto(saved);
        log.debug("User {} to UserDto entity {} was successful.", saved, result);

        return result;
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = getUserOrThrow(id);

        UserDto result = userMapper.toDto(user);
        log.debug("User {} to UserDto entity {} was successful.", user, result);

        return result;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        log.info("All users retrieved.");

        return users.stream()
                .map(userMapper::toDto)
                .peek(dto -> log.debug("Mapped to UserDto: {}", dto))
                .toList();
    }

    public void deleteById(Long id) {
        User user = getUserOrThrow(id);

        userRepository.delete(user);
        log.info("{} user was deleted", user);
    }

    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format(ErrorMessage.USER_NOT_FOND_EMAIL, email)));
        log.info("User with {} email retrieved.", email);

        UserDto result = userMapper.toDto(user);
        log.debug("User {} to UserDto entity {} was successful.", user, result);

        return result;
    }

    @Transactional(readOnly = true)
    public List<UserDto> findByName(String name) {
        List<User> users = userRepository.findByName(name);
        log.info("Users by {} name retrieved.", name);

        return users.stream()
                .map(userMapper::toDto)
                .peek(dto -> log.debug("Mapped to UserDto: {}", dto))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserDto> findByAge(Integer age) {
        List<User> users = userRepository.findByAge(age);
        log.info("Users by {} age retrieved.", age);

        return users.stream()
                .map(userMapper::toDto)
                .peek(dto -> log.debug("Mapped to UserDto: {}", dto))
                .toList();
    }

    private User getUserOrThrow(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format(ErrorMessage.USER_NOT_FOND_ID, id)));
        log.info("User with {} id retrieved.", id);
        return user;
    }
}