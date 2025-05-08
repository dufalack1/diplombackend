package by.grsu.eventlink.exception.joinrequest;

public class NodeNotInvitedException extends RuntimeException {

    private static final String ERROR_MESSAGE =
            "User (%s) are not invited to Node (%s)";

    public NodeNotInvitedException(Long userId, Long nodeId) {
        super(String.format(ERROR_MESSAGE, userId.toString(), nodeId.toString()));
    }

}
