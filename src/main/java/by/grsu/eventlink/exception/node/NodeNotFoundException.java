package by.grsu.eventlink.exception.node;

public class NodeNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE =
            "Node with id:%s was not found";

    public NodeNotFoundException(Long id) {
        super(String.format(ERROR_MESSAGE, id.toString()));
    }

}
