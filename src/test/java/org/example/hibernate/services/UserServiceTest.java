package org.example.hibernate.services;

import org.example.hibernate.dao.UserRepository;
import org.example.hibernate.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static UserRepository userRepository;
    private static UserService userService;
    private static User user;
    private static List<User> users;


    @BeforeAll
    static void setUp() {
        userRepository = mock();
        userService = new UserService(userRepository);
        user = new User("Liza", "1@4.com", 13);
        users = List.of(user);
    }

    @Test
    void when_save_then_verifySaveUserDaoCall() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User saved = userService.save(user);
        assertEquals(saved, user);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void when_update_then_verifyUpdateUserDaoCall() {
        when(userRepository.update(any(User.class))).thenReturn(true);
        boolean answer = userService.update(user);
        assertTrue(answer);
        verify(userRepository).update(any(User.class));
    }

    @Test
    void when_findById_then_verifyFindByIdUserDaoCall() {
        when(userRepository.findById(any(Long.class))).thenReturn(user);
        User found = userService.findById(12L);
        assertEquals(found, user);
        verify(userRepository).findById(any(Long.class));
    }

    @Test
    void when_findAll_then_verifyFindAllUserDaoCall() {
        when(userRepository.findAll()).thenReturn(users);
        List<User> found = userService.findAll();
        assertIterableEquals(found, users);
        verify(userRepository).findAll();
    }

    @Test
    void when_delete_then_verifyDeleteUserDaoCall() {
        when(userRepository.delete(any(User.class))).thenReturn(true);
        boolean answer = userService.delete(user);
        assertTrue(answer);
        verify(userRepository).delete(any(User.class));
    }

    @Test
    void when_deleteById_then_verifyDeleteByIdUserDaoCall() {
        when(userRepository.deleteById(any(Long.class))).thenReturn(true);
        boolean answer = userService.deleteById(12L);
        assertTrue(answer);
        verify(userRepository).deleteById(any(Long.class));
    }

    @Test
    void when_findByEmail_then_verifyFindByEmailUserDaoCall() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(user);
        User found = userService.findByEmail(user.getEmail());
        assertNotNull(found);
        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void when_findByName_then_verifyFindByNameUserDaoCall() {
        when(userRepository.findByName(any(String.class))).thenReturn(users);
        List<User> found = userService.findByName(user.getName());
        assertNotNull(found);
        assertFalse(found.isEmpty());
        verify(userRepository).findByName(any(String.class));
    }

    @Test
    void when_findByAge_then_verifyFindByAgeUserDaoCall() {
        when(userRepository.findByAge(any(Integer.class))).thenReturn(users);
        List<User> found = userService.findByAge(user.getAge());
        assertNotNull(found);
        assertFalse(found.isEmpty());
        verify(userRepository).findByAge(any(Integer.class));
    }
}
