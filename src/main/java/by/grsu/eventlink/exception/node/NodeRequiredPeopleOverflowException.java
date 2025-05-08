package by.grsu.eventlink.exception.node;

public class NodeRequiredPeopleOverflowException extends RuntimeException {

    private static final String ERROR_MESSAGE =
            "Node required people was overflow with %s";

    public NodeRequiredPeopleOverflowException(Integer requiredPeople) {
        super(String.format(ERROR_MESSAGE, requiredPeople.toString()));
    }

}
