package moheng.auth.exception;

public class EmptyBearerHeaderException extends RuntimeException {
    public EmptyBearerHeaderException(final String message) {
        super(message);
    }
}
