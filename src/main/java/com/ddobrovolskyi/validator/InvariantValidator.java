package com.ddobrovolskyi.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Validates correctness of certain conditions.
 * <p>
 * Evaluating is lazy which means that any condition won't be evaluated until calling eager/terminal operations:
 * {@link #isValid()}, {@link #throwIfNotValid(Function)}, {@link #ifNotValid(Consumer)}, {@link #getErrorMessage()}.
 * <p>
 * Evaluation terminates on the first fail.
 */
public class InvariantValidator {
    private final Map<Supplier<Boolean>, String> conditions = new LinkedHashMap<>();

    private InvariantValidator() {

    }

    public static InvariantValidator newInstance() {
        return new InvariantValidator();
    }

    /**
     * Validates the correctness based on passed {@code condition}.
     *
     * @param condition            Supplier which returns Boolean
     * @param errorMessageTemplate printf-style template of error message
     * @param errorMessageArgs     error message template arguments
     * @return InvariantValidator which is able to validate correctness based on passed {@code condition}.
     */
    public InvariantValidator validate(Supplier<Boolean> condition, String errorMessageTemplate, Object... errorMessageArgs) {
        conditions.put(condition, String.format(errorMessageTemplate, errorMessageArgs));
        return this;
    }

    /**
     * Validates that passed {@code value} is not {@literal null}.
     *
     * @param value                to check
     * @param errorMessageTemplate printf-style template of error message
     * @param errorMessageArgs     error message template arguments
     * @return InvariantValidator which is able to validate that passed {@code value} is not {@literal null}
     */
    public InvariantValidator validateNotNull(Object value, String errorMessageTemplate, Object... errorMessageArgs) {
        validate(() -> value != null, errorMessageTemplate, errorMessageArgs);
        return this;
    }

    /**
     * Validates that passed {@code value} is not empty ("") and not {@literal null}.
     *
     * @param value                value to check
     * @param errorMessageTemplate printf-style template of error message
     * @param errorMessageArgs     error message template arguments
     * @return InvariantValidator which is able to validate that passed {@code value} is not empty ("") and not {@literal null}
     */
    public InvariantValidator validateNotEmpty(String value, String errorMessageTemplate, Object... errorMessageArgs) {
        validate(() -> StringUtils.isNotEmpty(value), errorMessageTemplate, errorMessageArgs);
        return this;
    }

    /**
     * Validates that passed {@code value} is not empty (""), not {@literal null} and not whitespace only.
     *
     * @param value                value to check
     * @param errorMessageTemplate printf-style template of error message
     * @param errorMessageArgs     error message template arguments
     * @return InvariantValidator which is able to validate that passed {@code value} is not empty (""), not {@literal null} and not whitespace only.
     */
    public InvariantValidator validateNotBlank(String value, String errorMessageTemplate, Object... errorMessageArgs) {
        validate(() -> StringUtils.isNotBlank(value), errorMessageTemplate, errorMessageArgs);
        return this;
    }

    /**
     * Checks if the all passed conditions are valid.
     *
     * @return {@literal true} if all conditions are valid, {@literal false} otherwise.
     */
    public boolean isValid() {
        return conditions.entrySet().stream()
                .map(Map.Entry::getKey)
                .map(Supplier::get)
                .filter(Predicate.isEqual(false))
                .findFirst()
                .orElse(true);
    }

    /**
     * @return Optional of error message, which will contain value only if there was any validation failures.
     */
    public Optional<String> getErrorMessage() {
        return conditions.entrySet().stream()
                .filter(this::isConditionInvalid)
                .map(Map.Entry::getValue)
                .findFirst();
    }

    /**
     * Executes passed {@code action} only if there was any validation failures.
     *
     * @param action Consumer which takes error message as an argument
     */
    public void ifNotValid(Consumer<String> action) {
        conditions.entrySet().stream()
                .filter(this::isConditionInvalid)
                .findFirst()
                .ifPresent(condition -> action.accept(condition.getValue()));
    }

    /**
     * Throws passed {@code exception} only if there was any validation failures.
     *
     * @param exception Function which takes error message as an argument and return any RuntimeException
     */
    public void throwIfNotValid(Function<String, ? extends RuntimeException> exception) {
        conditions.entrySet().stream()
                .filter(this::isConditionInvalid)
                .findFirst()
                .ifPresent(condition -> {
                    throw exception.apply(condition.getValue());
                });
    }

    /**
     * Composes passed {@code validator} so that all its conditions are merged with {@code this} validator.
     *
     * @param validator InvariantValidator to compose
     * @return InvariantValidator which is able to validate all conditions of passed {@code validator} as well as conditions of {@code this} validator
     */
    public InvariantValidator compose(InvariantValidator validator) {
        validator.conditions.forEach((condition, errorMessage) -> conditions.merge(condition, errorMessage, (oldValue, newValue) -> newValue));
        return this;
    }

    private boolean isConditionInvalid(Map.Entry<Supplier<Boolean>, String> condition) {
        return !condition.getKey().get();
    }
}
