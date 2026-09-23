package org.example.spring.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.spring.dto.user_service.UserCreateUpdateDto;
import org.example.spring.dto.user_service.UserDto;
import org.example.spring.entities.User;
import org.example.spring.exception.UserAlreadyExistsException;
import org.example.spring.mappers.UserMapper;
import org.example.spring.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {
    private static final int USERS_COUNT = 2;
    private User userIrina1;
    private User userIrina2;

    @MockitoSpyBean
    private UserRepository userRepository;
    @MockitoSpyBean
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    @BeforeAll
    static void setUp() {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @BeforeEach
    void addUsers() {
        userIrina1 = new User("Irina", "test1@mail.ru", 13);
        userRepository.save(userIrina1);

        userIrina2 = new User(userIrina1.getName(), "test2@mail.ru", userIrina1.getAge());
        userRepository.save(userIrina2);

        clearInvocations(userRepository);
    }

    @Test
    void when_saveUser_then_returnUserDtoAndVerify() {
        UserCreateUpdateDto newUser = new UserCreateUpdateDto("Irina", "test3@mail.ru", 13);

        UserDto saved = userService.createUser(newUser);

        assertEquals(newUser.email(), saved.email());
        assertEquals(newUser.name(), saved.name());
        assertEquals(newUser.age(), saved.age());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toEntity(any(UserCreateUpdateDto.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    void when_saveUserWithDuplicateEmail_then_throwUserAlreadyExistsException() {
        UserCreateUpdateDto newUser = new UserCreateUpdateDto("Irina", userIrina1.getEmail(), 13);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(newUser));
    }

    @Test
    void when_updateExistentUser_then_returnUserDtoAndVerify() {
        String name = "Liza", email = "test4@mail.ru";
        int age = 60;
        UserCreateUpdateDto update = new UserCreateUpdateDto(name, email, age);

        UserDto updated = userService.updateUser(userIrina1.getId(), update);

        assertNotNull(updated);
        assertEquals(name, updated.name());
        assertEquals(email, updated.email());
        assertEquals(age, updated.age());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    void when_updateNonExistentUser_then_() {
        String name = "Liza", email = "test4@mail.ru";
        int age = 60;
        UserCreateUpdateDto update = new UserCreateUpdateDto(name, email, age);

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(9999L, update));
    }

    @Test
    void when_findByExistentId_then_returnUserDtoAndVerify() {
        UserDto found = userService.findById(userIrina1.getId());

        assertNotNull(found);
        verify(userRepository).findById(any(Long.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    void when_findByNonExistentId_then_throwEntityNotFoundExceptionAndVerify() {
        assertThrows(EntityNotFoundException.class, () -> userService.findById(9999L));
        verify(userRepository).findById(any(Long.class));
    }

    @Test
    void when_findAll_then_returnUsersDtoListAndVerify() {
        List<UserDto> found = userService.findAll();

        assertNotNull(found);
        assertEquals(USERS_COUNT, found.size());
        verify(userRepository).findAll();
        verify(userMapper, times(USERS_COUNT)).toDto(any(User.class));
    }

    @Test
    void when_deleteByExistentId_then_verify() {
        userService.deleteById(userIrina1.getId());

        verify(userRepository).delete(any(User.class));
    }

    @Test
    void when_deleteByNonExistentId_then_throwEntityNotFoundExceptionAndVerify() {
        assertThrows(EntityNotFoundException.class, () -> userService.deleteById(9999L));
    }

    @Test
    void when_findByExistentEmail_then_returnUserDtoAndVerify() {
        UserDto found = userService.findByEmail(userIrina1.getEmail());

        assertNotNull(found);
        verify(userRepository).findByEmail(any(String.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    void when_findByNonExistentEmail_then_throwEntityNotFoundExceptionAndVerify() {
        assertThrows(EntityNotFoundException.class, () -> userService.findByEmail("nonExistentTest@mail.ru"));
        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void when_findByName_then_returnUsersDtoListAndVerify() {
        List<UserDto> found = userService.findByName(userIrina1.getName());

        assertNotNull(found);
        assertEquals(2, found.size());
        verify(userRepository).findByName(any(String.class));
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    void when_findByAge_then_returnUsersDtoListAndVerify() {
        List<UserDto> found = userService.findByAge(userIrina1.getAge());

        assertNotNull(found);
        assertEquals(2, found.size());
        verify(userRepository).findByAge(any(Integer.class));
        verify(userMapper, times(2)).toDto(any(User.class));
    }
}
