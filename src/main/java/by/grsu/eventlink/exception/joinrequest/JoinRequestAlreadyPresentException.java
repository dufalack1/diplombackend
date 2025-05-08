package by.grsu.eventlink.exception.joinrequest;

public class JoinRequestAlreadyPresentException extends RuntimeException {

    private static final String ERROR_MESSAGE = "User %s already have join request to node %s";

    public JoinRequestAlreadyPresentException(Long userId, Long nodeId) {
        super(String.format(ERROR_MESSAGE, userId, nodeId));
    }

}
