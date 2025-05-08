package by.grsu.eventlink.exception.storage;

public class WrongPhotoFormatException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Wrong picture format";

    public WrongPhotoFormatException() {
        super(ERROR_MESSAGE);
    }

}
