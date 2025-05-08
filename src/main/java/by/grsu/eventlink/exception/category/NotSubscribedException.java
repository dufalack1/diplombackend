package by.grsu.eventlink.exception.category;

public class NotSubscribedException extends RuntimeException {

    private static final String ERROR_MESSAGE = "User with id:%s don't subscribed to category with id:%s";

    public NotSubscribedException(Long userId, Long categoryId) {
        super(String.format(ERROR_MESSAGE, userId.toString(), categoryId.toString()));
    }

}
