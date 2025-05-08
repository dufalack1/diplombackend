package by.grsu.eventlink.exception.category;

public class CategoryWithSameTitleAlreadyExistException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Category with same title already exist";

    public CategoryWithSameTitleAlreadyExistException() {
        super(ERROR_MESSAGE);
    }

}
