package by.grsu.eventlink.exception.category;

public class CategoryNotFoundException extends RuntimeException {

    private static final String GENERIC_MESSAGE = "Category id are not defined";

    private static final String ERROR_MESSAGE = "Category with id:%s was not found";

    public CategoryNotFoundException() {
        super(GENERIC_MESSAGE);
    }

    public CategoryNotFoundException(Long categoryId) {
        super(String.format(ERROR_MESSAGE, categoryId.toString()));
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

}
