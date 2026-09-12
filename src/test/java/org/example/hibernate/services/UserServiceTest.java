package org.example.hibernate.services;

import org.example.hibernate.dao.UserDao;
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
    private static UserDao userDao;
    private static UserService userService;
    private static User user;
    private static List<User> users;


    @BeforeAll
    static void setUp() {
        userDao = mock(UserDao.class);
        userService = new UserService(userDao);
        user = new User("Liza", "1@4.com", 13);
        users = List.of(user);
    }

    @Test
    void when_save_then_verifySaveUserDaoCall() {
        when(userDao.save(any(User.class))).thenReturn(user);
        User saved = userService.save(user);
        assertEquals(saved, user);
        verify(userDao).save(any(User.class));
    }

    @Test
    void when_update_then_verifyUpdateUserDaoCall() {
        when(userDao.update(any(User.class))).thenReturn(true);
        boolean answer = userService.update(user);
        assertTrue(answer);
        verify(userDao).update(any(User.class));
    }

    @Test
    void when_findById_then_verifyFindByIdUserDaoCall() {
        when(userDao.findById(any(Long.class))).thenReturn(user);
        User found = userService.findById(12L);
        assertEquals(found, user);
        verify(userDao).findById(any(Long.class));
    }

    @Test
    void when_findAll_then_verifyFindAllUserDaoCall() {
        when(userDao.findAll()).thenReturn(users);
        List<User> found = userService.findAll();
        assertIterableEquals(found, users);
        verify(userDao).findAll();
    }

    @Test
    void when_delete_then_verifyDeleteUserDaoCall() {
        when(userDao.delete(any(User.class))).thenReturn(true);
        boolean answer = userService.delete(user);
        assertTrue(answer);
        verify(userDao).delete(any(User.class));
    }

    @Test
    void when_deleteById_then_verifyDeleteByIdUserDaoCall() {
        when(userDao.deleteById(any(Long.class))).thenReturn(true);
        boolean answer = userService.deleteById(12L);
        assertTrue(answer);
        verify(userDao).deleteById(any(Long.class));
    }

    @Test
    void when_findByEmail_then_verifyFindByEmailUserDaoCall() {
        when(userDao.findByEmail(any(String.class))).thenReturn(user);
        User found = userService.findByEmail(user.getEmail());
        assertNotNull(found);
        verify(userDao).findByEmail(any(String.class));
    }

    @Test
    void when_findByName_then_verifyFindByNameUserDaoCall() {
        when(userDao.findByName(any(String.class))).thenReturn(users);
        List<User> found = userService.findByName(user.getName());
        assertNotNull(found);
        assertFalse(found.isEmpty());
        verify(userDao).findByName(any(String.class));
    }

    @Test
    void when_findByAge_then_verifyFindByAgeUserDaoCall() {
        when(userDao.findByAge(any(Integer.class))).thenReturn(users);
        List<User> found = userService.findByAge(user.getAge());
        assertNotNull(found);
        assertFalse(found.isEmpty());
        verify(userDao).findByAge(any(Integer.class));
    }
}
