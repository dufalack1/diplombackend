package by.grsu.eventlink.exception.subcategory;

public class SubCategoryNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Sub category with id:%s was not found";

    public SubCategoryNotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id.toString()));
    }

}
