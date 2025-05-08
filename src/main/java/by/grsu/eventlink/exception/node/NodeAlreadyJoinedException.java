package by.grsu.eventlink.exception.node;

public class NodeAlreadyJoinedException extends RuntimeException {

    private static final String ERROR_MESSAGE = "You are already joined!";

    public NodeAlreadyJoinedException() {
        super(ERROR_MESSAGE);
    }

}
