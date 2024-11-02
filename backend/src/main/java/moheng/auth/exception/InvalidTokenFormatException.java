package moheng.auth.exception;

public class InvalidTokenFormatException extends RuntimeException {
    public InvalidTokenFormatException(final String message) {
        super(message);
    }
}
