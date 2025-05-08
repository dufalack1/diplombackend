package by.grsu.eventlink.exception.user;

public class UserAlreadyExistException extends RuntimeException {

    public static final String ERROR_MESSAGE = "User with same data already exist";

    public UserAlreadyExistException() {
        super(ERROR_MESSAGE);
    }

}
