package by.grsu.eventlink.exception.user;

public class AgeLimitException extends RuntimeException {

    private static final String ERROR_MESSAGE = "You are too young for that event";

    public AgeLimitException() {
        super(ERROR_MESSAGE);
    }

}
