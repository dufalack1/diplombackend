package by.grsu.eventlink.exception.role;

public class UserDontHaveFollowingRoleException extends RuntimeException {

    private static final String ERROR_MESSAGE = "User with id %s don't have following role";

    private static final String SELF_REMOVE_MESSAGE = "You can't remove role from yourself";

    public UserDontHaveFollowingRoleException(Long userId) {
        super(String.format(ERROR_MESSAGE, userId));
    }

    public UserDontHaveFollowingRoleException() {
        super(SELF_REMOVE_MESSAGE);
    }

}
