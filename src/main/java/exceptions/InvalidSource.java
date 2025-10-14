package exceptions;

public class InvalidSource extends RuntimeException {
    public InvalidSource(String message) {
        super(message);
    }
}
