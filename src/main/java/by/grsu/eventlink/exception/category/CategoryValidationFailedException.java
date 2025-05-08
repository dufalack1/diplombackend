package by.grsu.eventlink.exception.category;

public class CategoryValidationFailedException extends RuntimeException {

    private static final String NULL_OBJECT = "null object";

    private static final String ERROR_MESSAGE = "Category validation failed cause of %s";

    public CategoryValidationFailedException(String component) {
        super(String.format(ERROR_MESSAGE, component));
    }

    public CategoryValidationFailedException() {
        super(String.format(ERROR_MESSAGE, NULL_OBJECT));
    }

}
