package by.grsu.eventlink.exception.comment;

public class EmptyCommentException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Comment you are trying to leave are empty";

    public EmptyCommentException() {
        super(ERROR_MESSAGE);
    }

}
