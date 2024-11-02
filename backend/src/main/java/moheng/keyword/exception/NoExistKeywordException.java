package moheng.keyword.exception;

public class NoExistKeywordException extends RuntimeException {
    public NoExistKeywordException(final String message) {
        super(message);
    }

    public NoExistKeywordException() {
        super("존재하지 않는 키워드입니다.");
    }
}
