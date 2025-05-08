package by.grsu.eventlink.exception.invitation;

public class InvitationNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Following user are not invited or it's out of activation period";

    public InvitationNotFoundException() {
        super(ERROR_MESSAGE);
    }

}
