package moheng.auth.exception;

public class NoExistMemberTokenException extends RuntimeException {
    public NoExistMemberTokenException(final String message) {
        super(message);
    }
}
