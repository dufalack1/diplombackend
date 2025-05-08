package by.grsu.eventlink.exception.comment;

public class CommentNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Comment with id:%s was not found";

    public CommentNotFoundException(Long commentId) {
        super(String.format(ERROR_MESSAGE, commentId.toString()));
    }

}
