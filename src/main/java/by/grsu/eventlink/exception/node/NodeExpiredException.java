package by.grsu.eventlink.exception.node;

public class NodeExpiredException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Node with id %s, you're trying to join is expired";

    public NodeExpiredException(Long nodeId) {
        super(String.format(ERROR_MESSAGE, nodeId.toString()));
    }

}
