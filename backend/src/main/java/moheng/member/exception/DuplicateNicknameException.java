package moheng.member.exception;

public class DuplicateNicknameException extends RuntimeException {
    public DuplicateNicknameException(final String message) {
        super(message);
    }
}
