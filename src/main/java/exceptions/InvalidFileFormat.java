package exceptions;

public class InvalidFileFormat extends RuntimeException {
    public InvalidFileFormat(String message) {
        super(message);
    }
}
