package by.grsu.eventlink.exception.role;

public class UserAlreadyHaveFollowingRoleException extends RuntimeException {

    private static final String SELF_GRANT_MESSAGE = "You cannot grant role to yourself";

    private static final String ERROR_MESSAGE = "User with id:%s already have following role";

    public UserAlreadyHaveFollowingRoleException(Long id) {
        super(String.format(ERROR_MESSAGE, id.toString()));
    }

    public UserAlreadyHaveFollowingRoleException() {
        super(SELF_GRANT_MESSAGE);
    }

}
