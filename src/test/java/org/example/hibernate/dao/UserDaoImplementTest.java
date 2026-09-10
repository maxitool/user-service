package org.example.hibernate.dao;

import org.example.hibernate.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
public class UserDaoImplementTest extends AbstractIntegrationTest {
    private UserDaoImplement userDao;

    private User userIrina1;
    private User userIrina2;
    private User userIvan;

    @BeforeEach
    void setUpDao() {
        userDao = new UserDaoImplement();

        userIrina1 = new User("Irina", "1@1.com", 13);
        userDao.save(userIrina1);

        userIrina2 = new User("Irina", "1@2.com", 25);
        userDao.save(userIrina2);

        userIvan = new User("Ivan", "1@3.com", 38);
        userDao.save(userIvan);
    }

    @Test
    void when_saveUser_then_returnFoundUser() {
        User newUser = new User("Liza", "1@4.com", 13);
        userDao.save(newUser);
        User found = userDao.findById(newUser.getId());
        assertNotNull(found);
        assertEquals("Liza", found.getName());
        assertEquals("1@4.com", found.getEmail());
    }

    @Test
    void when_findByEmail_then_returnFoundUser() {
        User found = userDao.findByEmail(userIrina2.getEmail());
        assertNotNull(found);
        assertEquals("Irina", found.getName());
        assertEquals("1@2.com", found.getEmail());
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
        boolean hasFirstEmail = result.stream().anyMatch(u -> u.getEmail().equals("1@1.com"));
        boolean hasSecondEmail = result.stream().anyMatch(u -> u.getEmail().equals("1@2.com"));

        assertTrue(hasFirstEmail);
        assertTrue(hasSecondEmail);
    }

    @Test
    void when_findByName_then_returnListUserEmpty() {
        List<User> result = userDao.findByName("Lara");

        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    void when_findByAge_then_returnListUser() {
        List<User> result = userDao.findByAge(13);

        assertNotNull(result);
        boolean hasFirstEmail = result.stream().anyMatch(u -> u.getEmail().equals("1@1.com"));
        assertTrue(hasFirstEmail);
    }

    @Test
    void when_findByAge_then_returnListEmpty() {
        List<User> result = userDao.findByAge(33);

        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    void when_update_then_return_true() {
        User userUpdate = userDao.findByEmail("1@1.com");
        userUpdate.setName("Lara");
        userDao.update(userUpdate);
        User userResultUpdate = userDao.findByEmail("1@1.com");

        assertEquals(userUpdate.getEmail(), userResultUpdate.getEmail());
        assertEquals(userUpdate.getId(), userResultUpdate.getId());
        assertEquals("Lara", userUpdate.getName());
    }

    @Test
    void when_updateNullEntity_then_returnFalse() {
        boolean result = userDao.update(null);

        assertFalse(result);
    }

    @Test
    void when_updateNonExistentEntity_then_returnFalse() {
        User nonExistentUser = new User("NonExistent", "test@test.com", 20);
        nonExistentUser.setId(99999L);
        boolean result = userDao.update(nonExistentUser);

        assertFalse(result);
    }

    @Test
    void when_findAll_then_listUser() {
        List<User> result = userDao.findAll();

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void when_delete_then_true() {
        User deleteUser = new User("IrinaForDelete", "delete@test.com", 25);
        boolean isDeleted = userDao.delete(deleteUser);
        assertTrue(isDeleted);

        User afterDelete = userDao.findById(deleteUser.getId());
        assertNull(afterDelete);
    }

    @Test
    void when_delete_then_false() {
        boolean isDeleted = userDao.delete(null);
        assertFalse(isDeleted);
    }

    @Test
    void when_deleteById_then_true() {
        User deleteUser = new User("IrinaForDelete", "delete@test.com", 25);
        userDao.save(deleteUser);

        boolean isDeleted = userDao.deleteById(deleteUser.getId());
        assertTrue(isDeleted);

        User afterDelete = userDao.findById(deleteUser.getId());
        assertNull(afterDelete);
    }

    @Test
    void when_deleteById_then_false() {
        boolean isDeleted = userDao.deleteById(9999L);
        assertFalse(isDeleted);
    }
}
