package org.example.hibernate.dao;

import org.example.AbstractIntegrationTest;
import org.example.hibernate.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
public class UserDaoImplementTest extends AbstractIntegrationTest {
    private static UserDaoImplement userDao;

    private static User userIrina1;
    private static User userIrina2;

    @BeforeAll
    static void setUpDao() {
        userDao = new UserDaoImplement();

        userIrina1 = new User("Irina", "1@1.com", 13);
        userDao.save(userIrina1);

        userIrina2 = new User("Irina", "1@2.com", 25);
        userDao.save(userIrina2);
    }

    @Test
    void when_saveUser_then_returnFoundUser() {
        User newUser = new User("Liza", "1@4.com", 13);
        newUser = userDao.save(newUser);
        assertNotNull(newUser);
        User found = userDao.findById(newUser.getId());

        assertNotNull(found);
        assertEquals(newUser.getName(), found.getName());
        assertEquals(newUser.getEmail(), found.getEmail());
        assertEquals(newUser.getAge(), found.getAge());
        assertEquals(newUser.getCreatedAt(), found.getCreatedAt());

        userDao.deleteById(found.getId());
    }

    @Test
    void when_saveUserWithDuplicateEmail_then_returnNull() {
        User duplicateUser = new User("Duplicate", userIrina1.getEmail(), 20);
        User savedUser = userDao.save(duplicateUser);
        assertNull(savedUser);

        User found = userDao.findByEmail(duplicateUser.getEmail());
        assertNotNull(found);
        assertNotEquals(found.getName(), duplicateUser.getName());
    }

    @Test
    void when_saveNull_then_returnNull() {
        User savedUser = userDao.save(null);
        assertNull(savedUser);
    }


    @Test
    void when_findByEmail_then_returnFoundUser() {
        User found = userDao.findByEmail(userIrina2.getEmail());
        assertNotNull(found);
        assertEquals(userIrina2.getId(), found.getId());
        assertEquals(userIrina2.getName(), found.getName());
        assertEquals(userIrina2.getAge(), found.getAge());
        assertEquals(userIrina2.getCreatedAt(), found.getCreatedAt());
    }

    @Test
    void when_findByEmail_then_userNotFound() {
        User result = userDao.findByEmail("2@2.com");
        assertNull(result);
    }

    @Test
    void when_findByName_then_returnListUser() {
        List<User> result = userDao.findByName("Irina");
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(userIrina1.getEmail())));
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(userIrina2.getEmail())));
    }

    @Test
    void when_findByName_then_returnListUserEmpty() {
        List<User> result = userDao.findByName("Lara");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void when_findByAge_then_returnListUser() {
        List<User> result = userDao.findByAge(userIrina1.getAge());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(userIrina1.getEmail())));
    }

    @Test
    void when_findByAge_then_returnListEmpty() {
        List<User> result = userDao.findByAge(33);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void when_update_then_returnTrue() {
        User userUpdate = userDao.findByEmail(userIrina1.getEmail());
        assertNotNull(userUpdate);

        userUpdate.setName("Lara");
        boolean answer = userDao.update(userUpdate);
        assertTrue(answer);

        User userResultUpdate = userDao.findByEmail(userIrina1.getEmail());

        assertNotNull(userResultUpdate);
        assertEquals(userUpdate.getId(), userResultUpdate.getId());
        assertEquals(userUpdate.getEmail(), userResultUpdate.getEmail());
        assertEquals("Lara", userUpdate.getName());
        assertEquals(userUpdate.getAge(), userResultUpdate.getAge());
        assertEquals(userUpdate.getCreatedAt(), userResultUpdate.getCreatedAt());

        userUpdate.setName("Irina");
        userDao.update(userUpdate);
    }

    @Test
    void when_updateNullEntity_then_returnFalse() {
        boolean result = userDao.update(null);
        assertFalse(result);
    }

    @Test
    void when_updateNonExistentEntity_then_saveItAndReturnTrue() {
        User nonExistentUser = new User("NonExistent", "test@test.com", 20);
        boolean answer = userDao.update(nonExistentUser);
        assertTrue(answer);

        nonExistentUser = userDao.findByEmail(nonExistentUser.getEmail());
        userDao.delete(nonExistentUser);
    }

    @Test
    void when_findAll_then_returnListUser() {
        List<User> result = userDao.findAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void when_deleteEntity_then_returnTrue() {
        User deleteUser = new User("IrinaForDelete", "delete@test.com", 25);
        deleteUser = userDao.save(deleteUser);
        assertNotNull(deleteUser);

        boolean isDeleted = userDao.delete(deleteUser);
        assertTrue(isDeleted);

        User afterDelete = userDao.findById(deleteUser.getId());
        assertNull(afterDelete);
    }

    @Test
    void when_deleteNull_then_returnFalse() {
        boolean isDeleted = userDao.delete(null);
        assertFalse(isDeleted);
    }

    @Test
    void when_deleteByExistentId_then_returnTrue() {
        User deleteUser = new User("IrinaForDelete", "delete@test.com", 25);
        userDao.save(deleteUser);

        boolean isDeleted = userDao.deleteById(deleteUser.getId());
        assertTrue(isDeleted);

        User afterDelete = userDao.findById(deleteUser.getId());
        assertNull(afterDelete);
    }

    @Test
    void when_deleteByNotExistentId_then_returnFalse() {
        boolean isDeleted = userDao.deleteById(99999L);
        assertFalse(isDeleted);
    }
}
