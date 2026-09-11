package hibernate.validator;

import org.example.hibernate.entities.User;
import org.example.hibernate.validator.ValidatorUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidatorUtil: тесты валидации сущности User")
class ValidatorUtilTest {

    @Nested
    @DisplayName("when_userIsValid_then_returnTrue")
    class ValidUserTests {

        @Test
        @DisplayName("when_userWithAllFieldsValid_then_returnTrue")
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
        @DisplayName("when_emailIsValid_then_returnTrue")
        void when_emailIsValid_then_returnTrue(String email) {

            User user = new User("Катя", email, 45);

            boolean result = ValidatorUtil.validate(user);

            assertTrue(result);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 18, 100, Integer.MAX_VALUE})
        @DisplayName("when_ageIsPositive_then_returnTrue")
        void when_ageIsPositive_then_returnTrue(int age) {

            User user = new User("Маша", "masha@masha.com", age);

            boolean result = ValidatorUtil.validate(user);

            assertTrue(result);
        }
    }


    @Nested
    @DisplayName("when_userIsInvalid_then_returnFalse")
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
        @DisplayName("when_emailFormatIsIncorrect_then_returnFalse")
        void when_emailFormatIsIncorrect_then_returnFalse(String email) {
            User user = new User("Катя", email, 25);
            assertFalse(ValidatorUtil.validate(user));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -100, Integer.MIN_VALUE})
        @DisplayName("when_ageIsNotPositive_then_returnFalse")
        void when_ageIsNotPositive_then_returnFalse(int age) {

            User user = new User("Катя", "katya@example.com", age);

            boolean result = ValidatorUtil.validate(user);

            assertFalse(result);
        }

        @Test
        @DisplayName("when_multipleViolationsExist_then_returnFalse")
        void when_multipleViolationsExist_then_returnFalse() {

            User user = new User("Катя", "bad-email", -5);

            boolean result = ValidatorUtil.validate(user);

            assertFalse(result);
        }
    }


    @Nested
    @DisplayName("when_edgeCaseOccurs_then_followJakartaSpec")
    class EdgeCaseTests {

        @Test
        @DisplayName("when_ageIsNull_then_returnTrue_becausePositiveIgnoresNull")
        void when_ageIsNull_then_returnTrue() {

            User user = new User("Тест", "test@example.com", null);

            boolean result = ValidatorUtil.validate(user);

            assertTrue(result);
        }

        @Test
        @DisplayName("when_emailIsNull_then_returnTrue_becauseEmailIgnoresNull")
        void when_emailIsNull_then_returnTrue() {

            User user = new User("Тест", null, 25);

            boolean result = ValidatorUtil.validate(user);

            assertTrue(result);
        }

        @Test
        @DisplayName("when_emailIsEmpty_then_returnTrue_becauseEmailAcceptsEmpty")
        void when_emailIsEmpty_then_returnTrue() {

            User user = new User("Тест", "", 25);

            boolean result = ValidatorUtil.validate(user);

            assertTrue(result);
        }

        @Test
        @DisplayName("when_emailHasNoDotInDomain_then_returnTrue_becauseJakartaEmailAllowsIt")
        void when_emailHasNoDotInDomain_then_returnTrue() {

            User user = new User("Катя", "user@domain", 25);

            boolean result = ValidatorUtil.validate(user);

            assertTrue(result);
        }


        @Test
        @DisplayName("when_nameIsNull_then_returnTrue_becauseNoNotBlankAnnotation")
        void when_nameIsNull_then_returnTrue() {

            User user = new User(null, "test@example.com", 25);

            boolean result = ValidatorUtil.validate(user);

            assertTrue(result);
        }

        @Test
        @DisplayName("when_objectIsNull_then_throwIllegalArgumentException")
        void when_objectIsNull_then_throwIllegalArgumentException() {

            User nullUser = null;

            assertThrows(IllegalArgumentException.class,
                    () -> ValidatorUtil.validate(nullUser));
        }
    }
}