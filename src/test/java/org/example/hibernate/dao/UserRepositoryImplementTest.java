package org.example.hibernate.dao;

import org.example.hibernate.config.HibernateUtil;
import org.example.hibernate.entities.User;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Testcontainers
public class UserRepositoryImplementTest extends AbstractIntegrationTest {

    @Nested
    class GoodSessionFactoryTest {
        private UserRepositoryImplement userDao;

        private User userIrina1;
        private User userIrina2;

        @BeforeEach
        void SetUpDao() {
            userDao = new UserRepositoryImplement(HibernateUtil.getSessionFactory());

            userIrina1 = new User("Irina", "1@1.com", 13);
            userDao.save(userIrina1);

            userIrina2 = new User(userIrina1.getName(), "1@2.com", 25);
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
        void when_findByExistentId_then_returnFoundUser() {
            User found = userDao.findById(1L);

            assertNotNull(found);
        }

        @Test
        void when_findByNonExistentId_then_returnNull() {
            User found = userDao.findById(9999L);

            assertNull(found);
        }

        @Test
        void when_findByNullId_then_returnNull() {
            User found = userDao.findById(null);

            assertNull(found);
        }

        @Test
        void when_findByExistentEmail_then_returnFoundUser() {
            User found = userDao.findByEmail(userIrina2.getEmail());

            assertNotNull(found);
            assertEquals(userIrina2.getId(), found.getId());
            assertEquals(userIrina2.getName(), found.getName());
            assertEquals(userIrina2.getAge(), found.getAge());
            assertEquals(userIrina2.getCreatedAt(), found.getCreatedAt());
        }

        @Test
        void when_findByNonExistentEmail_then_returnNull() {
            User result = userDao.findByEmail("2@2.com");

            assertNull(result);
        }

        @Test
        void when_findByNullEmail_then_returnNull() {
            User result = userDao.findByEmail(null);

            assertNull(result);
        }

        @Test
        void when_findByExistentName_then_returnUsersList() {
            List<User> result = userDao.findByName(userIrina1.getName());

            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(userIrina1.getEmail())));
            assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(userIrina2.getEmail())));
        }

        @Test
        void when_findByNonExistentName_then_returnUsersListEmpty() {
            List<User> result = userDao.findByName("Lara");

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void when_findByNullName_then_returnUsersListEmpty() {
            List<User> result = userDao.findByName(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void when_findByExistentAge_then_returnUsersList() {
            List<User> result = userDao.findByAge(userIrina1.getAge());

            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertTrue(result.stream().anyMatch(u -> u.getEmail().equals(userIrina1.getEmail())));
        }

        @Test
        void when_findByNonExistentAge_then_returnUsersListEmpty() {
            List<User> result = userDao.findByAge(33);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void when_findByNullAge_then_returnUsersListEmpty() {
            List<User> result = userDao.findByAge(null);

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void when_findAll_then_returnUsersList() {
            List<User> result = userDao.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
        }


        @Test
        void when_updateExistentEntity_then_returnTrue() {
            String name = "Lara";
            userIrina1.setName(name);

            boolean answer = userDao.update(userIrina1);

            assertTrue(answer);
            User userResultUpdate = userDao.findByEmail(userIrina1.getEmail());
            assertNotNull(userResultUpdate);
            assertEquals(userIrina1.getId(), userResultUpdate.getId());
            assertEquals(userIrina1.getEmail(), userResultUpdate.getEmail());
            assertEquals(name, userIrina1.getName());
            assertEquals(userIrina1.getAge(), userResultUpdate.getAge());
            assertEquals(userIrina1.getCreatedAt(), userResultUpdate.getCreatedAt());
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
        }


        @Test
        void when_deleteEntity_then_returnTrue() {
            boolean isDeleted = userDao.delete(userIrina1);

            assertTrue(isDeleted);
            User afterDelete = userDao.findById(userIrina1.getId());
            assertNull(afterDelete);
        }

        @Test
        void when_deleteNull_then_returnFalse() {
            boolean isDeleted = userDao.delete(null);

            assertFalse(isDeleted);
        }

        @Test
        void when_deleteByExistentId_then_returnTrue() {
            boolean isDeleted = userDao.deleteById(userIrina1.getId());

            assertTrue(isDeleted);
            User afterDelete = userDao.findById(userIrina1.getId());
            assertNull(afterDelete);
        }

        @Test
        void when_deleteByNonExistentId_then_returnFalse() {
            boolean isDeleted = userDao.deleteById(99999L);

            assertFalse(isDeleted);
        }

        @Test
        void when_deleteByNullId_then_returnFalse() {
            boolean isDeleted = userDao.deleteById(null);

            assertFalse(isDeleted);
        }
    }


    @Nested
    @Tag("SkipInitDatabase")
    class BadSessionFactoryTest {
        private static SessionFactory sessionFactoryMock;
        private static UserRepositoryImplement badSessionFactoryDao;
        private static User user;

        @BeforeAll
        static void SetUpSessionFactoryMock() {
            sessionFactoryMock = mock();
            when(sessionFactoryMock.openSession()).thenThrow(new HibernateException("HibernateException"));
            badSessionFactoryDao = new UserRepositoryImplement(sessionFactoryMock);
        }

        @BeforeEach
        void setUpUser() {
            user = new User("Liza", "1@4.com", 13);
        }

        @AfterEach
        void resetMocks() {
            reset(sessionFactoryMock);
        }


        @Test
        void when_initUserDaoWithNull_then_throwExceptionInInitializerError() {
            assertThrows(ExceptionInInitializerError.class, this::initUserDaoWithNull);
        }

        private void initUserDaoWithNull() {
            new UserRepositoryImplement(null);
        }


        @Test
        void when_saveUser_then_verifyOpenSessionAndReturnNull() {
            user = badSessionFactoryDao.save(user);

            verify(sessionFactoryMock).openSession();
            assertNull(user);
        }

        @Test
        void when_findById_then_verifyOpenSessionAndReturnNull() {
            User found = badSessionFactoryDao.findById(1L);

            verify(sessionFactoryMock).openSession();
            assertNull(found);
        }

        @Test
        void when_findByEmail_then_verifyOpenSessionAndReturnNull() {
            User found = badSessionFactoryDao.findByEmail(user.getEmail());

            verify(sessionFactoryMock).openSession();
            assertNull(found);
        }

        @Test
        void when_findByName_then_verifyOpenSessionAndReturnUsersListEmpty() {
            List<User> result = badSessionFactoryDao.findByName(user.getName());

            verify(sessionFactoryMock).openSession();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }


        @Test
        void when_findByAge_then_verifyOpenSessionAndReturnUsersListEmpty() {
            List<User> result = badSessionFactoryDao.findByAge(user.getAge());

            verify(sessionFactoryMock).openSession();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void when_findAll_then_verifyOpenSessionAndReturnUsersListEmpty() {
            List<User> result = badSessionFactoryDao.findAll();

            verify(sessionFactoryMock).openSession();
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void when_update_then_verifyOpenSessionAndReturnFalse() {
            user.setName("name2");

            boolean answer = badSessionFactoryDao.update(user);

            verify(sessionFactoryMock).openSession();
            assertFalse(answer);
        }

        @Test
        void when_delete_then_verifyOpenSessionAndReturnFalse() {
            boolean isDeleted = badSessionFactoryDao.delete(user);

            verify(sessionFactoryMock).openSession();
            assertFalse(isDeleted);
        }

        @Test
        void when_deleteByExistentId_then_verifyOpenSessionAndReturnTrue() {
            boolean isDeleted = badSessionFactoryDao.deleteById(1L);

            verify(sessionFactoryMock).openSession();
            assertFalse(isDeleted);
        }
    }
}
