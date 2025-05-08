package by.grsu.eventlink.exception.subcategory;

public class SubCategoryAlreadyExist extends RuntimeException {

    private static final String ERROR_MESSAGE = "Sub category with following title already exist";

    public SubCategoryAlreadyExist() {
        super(ERROR_MESSAGE);
    }

}
