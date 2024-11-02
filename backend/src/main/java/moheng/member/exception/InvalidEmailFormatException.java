package moheng.member.exception;

public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException(final String message) {
        super(message);
    }
}
