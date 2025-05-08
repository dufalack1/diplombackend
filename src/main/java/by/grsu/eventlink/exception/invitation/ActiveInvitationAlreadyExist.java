package by.grsu.eventlink.exception.invitation;

public class ActiveInvitationAlreadyExist extends RuntimeException {

    private static final String ERROR_MESSAGE = "Active invitation already exist";

    public ActiveInvitationAlreadyExist() {
        super(ERROR_MESSAGE);
    }

}
