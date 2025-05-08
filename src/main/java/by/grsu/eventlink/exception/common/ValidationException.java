package by.grsu.eventlink.exception.common;

public class ValidationException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Field %s validation failed";

    public ValidationException(String field) {
        super(String.format(ERROR_MESSAGE, field));
    }

}
