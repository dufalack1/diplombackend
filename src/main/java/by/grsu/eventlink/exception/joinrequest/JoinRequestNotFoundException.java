package by.grsu.eventlink.exception.joinrequest;

public class JoinRequestNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE =
            "Join request for user (%s) for Node (%s) was not found";

    public JoinRequestNotFoundException(String email, Long nodeId) {
        super(String.format(ERROR_MESSAGE, email, nodeId.toString()));
    }

    public JoinRequestNotFoundException(Long userId, Long nodeId) {
        super(String.format(ERROR_MESSAGE, userId, nodeId.toString()));
    }

}
