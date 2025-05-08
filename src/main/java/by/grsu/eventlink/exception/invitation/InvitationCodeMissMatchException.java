package by.grsu.eventlink.exception.invitation;

public class InvitationCodeMissMatchException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Wrong invitation code";

    public InvitationCodeMissMatchException() {
        super(ERROR_MESSAGE);
    }

}
