package ru.javawebinar.topjava.repository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

public final class ValidatorUtil {
    private ValidatorUtil() {
    }

    private final static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
        if (!violations.isEmpty())
            throw new RuntimeException(violations.stream().map(ConstraintViolation::toString).collect(Collectors.joining("\n")));
    }
}
