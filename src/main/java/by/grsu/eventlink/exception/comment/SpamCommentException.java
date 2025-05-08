package by.grsu.eventlink.exception.comment;

public class SpamCommentException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Same comment already presented";

    public SpamCommentException() {
        super(ERROR_MESSAGE);
    }

}
