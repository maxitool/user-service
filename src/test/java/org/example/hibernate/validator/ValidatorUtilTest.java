package org.example.hibernate.validator;

import org.example.hibernate.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorUtilTest {

    @BeforeAll
    static void setProperties() {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @Nested
    class ValidUserTests {

        @Test
        void when_userWithAllFieldsValid_then_returnTrue() {
            User user = new User("Михаил Чебукинский", "misha@chebur.com", 25);
            boolean result = ValidatorUtil.validate(user);
            assertTrue(result);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "a@b.co",
                "user.name+tag@sub.domain.com",
                "test123@example.org"
        })
        void when_emailIsValid_then_returnTrue(String email) {
            User user = new User("Katya", email, 45);
            boolean result = ValidatorUtil.validate(user);
            assertTrue(result);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 18, 100, Integer.MAX_VALUE})
        void when_ageIsPositive_then_returnTrue(int age) {
            User user = new User("Masha", "masha@masha.com", age);
            boolean result = ValidatorUtil.validate(user);
            assertTrue(result);
        }
    }


    @Nested
    class InvalidUserTests {

        @ParameterizedTest
        @ValueSource(strings = {
                "not-an-email",
                "abc@",
                "@domain.com",
                "plain-text",
                "user@",
                "user@@domain.com",
                "user name@domain.com"
        })
        void when_emailFormatIsIncorrect_then_returnFalse(String email) {
            User user = new User("Katya", email, 25);
            assertFalse(ValidatorUtil.validate(user));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -100, Integer.MIN_VALUE})
        void when_ageIsNotPositive_then_returnFalse(int age) {
            User user = new User("Katya", "katya@example.com", age);
            boolean result = ValidatorUtil.validate(user);
            assertFalse(result);
        }

        @Test
        void when_multipleViolationsExist_then_returnFalse() {
            User user = new User("Katya", "bad-email", -5);
            boolean result = ValidatorUtil.validate(user);
            assertFalse(result);
        }

        @Test
        void when_ageIsNull_then_returnFalse() {
            User user = new User("Test", "test@example.com", null);
            boolean result = ValidatorUtil.validate(user);
            assertFalse(result);
        }

        @Test
        void when_emailIsNull_then_returnFalse() {
            User user = new User("Test", null, 25);
            boolean result = ValidatorUtil.validate(user);
            assertFalse(result);
        }

        @Test
        void when_emailIsEmpty_then_returnFalse() {
            User user = new User("Test", "", 25);
            boolean result = ValidatorUtil.validate(user);
            assertFalse(result);
        }

        @Test
        void when_emailLengthGreater100_then_returnFalse() {
            User user = new User("Test"
                    , "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiop@m.com"
                    , 25);
            boolean result = ValidatorUtil.validate(user);
            assertFalse(result);
        }

        @Test
        void when_nameIsNull_then_returnFalse() {
            User user = new User(null, "test@example.com", 25);
            boolean result = ValidatorUtil.validate(user);
            assertFalse(result);
        }

        @Test
        void when_nameLengthGreater100_then_returnFalse() {
            User user = new User(
                    "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopf"
                    , "test@example.com", 25);
            boolean result = ValidatorUtil.validate(user);
            assertFalse(result);
        }

        @Test
        void when_objectIsNull_then_throwIllegalArgumentException() {
            assertFalse(ValidatorUtil.validate(null));
        }
    }
}