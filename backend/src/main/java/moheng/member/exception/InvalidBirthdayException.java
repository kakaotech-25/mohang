package moheng.member.exception;

public class InvalidBirthdayException extends RuntimeException {
    public InvalidBirthdayException(final String message) {
        super(message);
    }
}
