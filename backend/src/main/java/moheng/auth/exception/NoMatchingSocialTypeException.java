package moheng.auth.exception;

public class NoMatchingSocialTypeException extends RuntimeException {
    public NoMatchingSocialTypeException(final String message) {
        super(message);
    }

    public NoMatchingSocialTypeException() {
        super("제공되지 않는 소셜 타입 제공처입니다.");
    }
}
