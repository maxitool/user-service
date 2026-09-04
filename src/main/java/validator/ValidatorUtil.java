package validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

/**
 * Утилитный класс для проверки объектов на соответствие ограничениям Jakarta Bean Validation.
 * Предоставляет методы для валидации полей по аннотациям (например, @NotNull, @Min).
 */
public class ValidatorUtil {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Проверяет переданный объект на наличие нарушений ограничений валидации.
     *
     * @param <T> тип проверяемого объекта
     * @param object объект, который необходимо провалидировать
     * @throws IllegalArgumentException если объект не прошел валидацию (текст исключения содержит сообщение первого найденного нарушения)
     */
    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException(violations.iterator().next().getMessage());
        }
    }
}
//TODO добавить в класс user валидацию над полями
// @Email(message = "Некорректный формат email"),
// @Positive(message = "Возраст должен быть больше нуля")