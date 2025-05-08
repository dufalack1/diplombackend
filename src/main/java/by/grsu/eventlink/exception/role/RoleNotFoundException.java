package by.grsu.eventlink.exception.role;

public class RoleNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Role with title %s was not found";

    public RoleNotFoundException(String title) {
        super(String.format(ERROR_MESSAGE, title));
    }

}
