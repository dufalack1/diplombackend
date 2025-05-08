package by.grsu.eventlink.exception.category;

public class EmptyQueryException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Searching query is empty";

    public EmptyQueryException() {
        super(ERROR_MESSAGE);
    }

}
