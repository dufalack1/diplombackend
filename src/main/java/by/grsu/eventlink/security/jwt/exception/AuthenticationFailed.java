package by.grsu.eventlink.security.jwt.exception;

public class AuthenticationFailed extends RuntimeException {

    private static final String ERROR_MESSAGE = "Authentication failed. Provided wrong data.";

    public AuthenticationFailed() {
        super(ERROR_MESSAGE);
    }

}
