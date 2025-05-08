package by.grsu.eventlink.exception.category;

public class AlreadySubscribedException extends RuntimeException {

    private static final String ERROR_MESSAGE = "User with id:%s already subscribed to category with id:%s";

    public AlreadySubscribedException(Long userId, Long categoryId) {
        super(String.format(ERROR_MESSAGE, userId.toString(), categoryId.toString()));
    }

}
