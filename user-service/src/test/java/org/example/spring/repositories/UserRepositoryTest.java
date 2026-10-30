package org.example.spring.repositories;

import jakarta.validation.ConstraintViolationException;
import org.example.spring.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User userIrina1;
    private User userIrina2;

    @BeforeAll
    static void setUp() {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @BeforeEach
    void addUsers() {
        userIrina1 = new User("Irina", "test1@mail.ru", 13);
        userRepository.save(userIrina1);

        userIrina2 = new User(userIrina1.getName(), "test2@mail.ru", 25);
        userRepository.save(userIrina2);
    }


    @Test
    void when_saveUser_then_returnFoundUser() {
        User newUser = new User("Liza", "test3@mail.ru", 13);

        newUser = userRepository.save(newUser);

        assertNotNull(newUser);
        Optional<User> found = userRepository.findById(newUser.getId());
        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(newUser.getName(), found.get().getName());
        assertEquals(newUser.getEmail(), found.get().getEmail());
        assertEquals(newUser.getAge(), found.get().getAge());
        assertEquals(newUser.getCreatedAt(), found.get().getCreatedAt());
    }

    @Test
    void when_saveUserWithDuplicateEmail_then_returnThrowDataIntegrityViolationException() {
        User duplicateUser = new User("Duplicate", userIrina1.getEmail(), 20);

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(duplicateUser));
    }

    @Test
    void when_saveUserWithNegativeAge_then_returnThrowDataIntegrityViolationException() {
        User negativeAgeUser = new User("NegativeAge", "negativeAge@mail.ru", -20);

        assertThrows(ConstraintViolationException.class, () -> userRepository.save(negativeAgeUser));
    }

    @Test
    void when_saveNull_then_returnThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.save(null));
    }

    @Test
    void when_findByExistentId_then_returnFoundUser() {
        Optional<User> found = userRepository.findById(userIrina1.getId());

        assertNotNull(found);
        assertFalse(found.isEmpty());
    }

    @Test
    void when_findByNonExistentId_then_returnNull() {
        Optional<User> found = userRepository.findById(9999L);

        assertNotNull(found);
        assertTrue(found.isEmpty());
    }

    @Test
    void when_findByNullId_then_returnThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.findById(null));
    }

    @Test
    void when_findByExistentEmail_then_returnFoundUser() {
        Optional<User> found = userRepository.findByEmail(userIrina2.getEmail());

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(userIrina2.getId(), found.get().getId());
        assertEquals(userIrina2.getName(), found.get().getName());
        assertEquals(userIrina2.getAge(), found.get().getAge());
        assertEquals(userIrina2.getCreatedAt(), found.get().getCreatedAt());
    }

    @Test
    void when_findByNonExistentEmail_then_returnNull() {
        Optional<User> result = userRepository.findByEmail("nonExistentTest@mail.ru");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void when_findByNullEmail_then_returnNull() {
        Optional<User> result = userRepository.findByEmail(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void when_findByExistentName_then_returnUsersList() {
        List<User> result = userRepository.findByName(userIrina1.getName());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(userIrina1.getEmail())));
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(userIrina2.getEmail())));
    }

    @Test
    void when_findByNonExistentName_then_returnUsersListEmpty() {
        List<User> result = userRepository.findByName("Lara");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void when_findByNullName_then_returnUsersListEmpty() {
        List<User> result = userRepository.findByName(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void when_findByExistentAge_then_returnUsersList() {
        List<User> result = userRepository.findByAge(userIrina1.getAge());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(userIrina1.getEmail())));
    }

    @Test
    void when_findByNonExistentAge_then_returnUsersListEmpty() {
        List<User> result = userRepository.findByAge(33);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void when_findByNullAge_then_returnUsersListEmpty() {
        List<User> result = userRepository.findByAge(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void when_findAll_then_returnUsersList() {
        List<User> result = userRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }


    @Test
    void when_updateEntity() {
        String name = "Lara", email = "newEmailTest@mail.ru";
        int age = 60;
        userIrina1.setName(name);
        userIrina1.setEmail(email);
        userIrina1.setAge(age);

        User userResultUpdate = userRepository.save(userIrina1);
        assertNotNull(userResultUpdate);
        assertEquals(userIrina1.getId(), userResultUpdate.getId());
        assertEquals(email, userResultUpdate.getEmail());
        assertEquals(name, userIrina1.getName());
        assertEquals(age, userResultUpdate.getAge());
        assertEquals(userIrina1.getCreatedAt(), userResultUpdate.getCreatedAt());
    }

    @Test
    void when_deleteEntity_then_returnTrue() {
        userRepository.delete(userIrina1);

        Optional<User> afterDelete = userRepository.findById(userIrina1.getId());
        assertNotNull(afterDelete);
        assertTrue(afterDelete.isEmpty());
    }

    @Test
    void when_deleteNull_then_returnThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.delete(null));
    }

    @Test
    void when_deleteByExistentId_then_returnTrue() {
        userRepository.deleteById(userIrina1.getId());

        Optional<User> afterDelete = userRepository.findById(userIrina1.getId());
        assertNotNull(afterDelete);
        assertTrue(afterDelete.isEmpty());
    }

    @Test
    void when_deleteByNullId_then_returnThrowInvalidDataAccessApiUsageException() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userRepository.deleteById(null));
    }
}
