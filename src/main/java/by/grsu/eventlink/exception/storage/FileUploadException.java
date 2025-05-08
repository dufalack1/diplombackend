package by.grsu.eventlink.exception.storage;

public class FileUploadException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Cannot upload file: %s";

    public FileUploadException(String filename) {
        super(String.format(ERROR_MESSAGE, filename));
    }

}
