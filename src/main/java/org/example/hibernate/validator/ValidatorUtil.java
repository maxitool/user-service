package org.example.hibernate.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class ValidatorUtil {

    private static final Validator VALIDATOR;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            VALIDATOR = factory.getValidator();
        } catch (Throwable ex) {
            System.err.println("Initial validator creation failed." + ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static <T> boolean validate(T object) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
        if (!violations.isEmpty()) {
            System.err.println(violations.iterator().next().getMessage());
            return false;
        }
        return true;
    }
}