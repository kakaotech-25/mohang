package moheng.member.exception;

public class InvalidNicknameFormatException extends RuntimeException {
    public InvalidNicknameFormatException(final String message) {
        super(message);
    }
}
