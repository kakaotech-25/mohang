package moheng.auth.exception;

public class NoExistMemberTokenException extends RuntimeException {
    public NoExistMemberTokenException(String message) {
        super(message);
    }
}
