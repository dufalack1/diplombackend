package by.grsu.eventlink.exception.common;

public class InternalServerError extends RuntimeException {

    private final static String GENERIC_MESSAGE = "Something went wrong";

    public InternalServerError() {
        super(GENERIC_MESSAGE);
    }

    public InternalServerError(String message) {
        super(message);
    }

}
