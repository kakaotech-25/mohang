package moheng.auth.exception;

public class NoExistOAuthClientException extends RuntimeException {
    public NoExistOAuthClientException(final String message) {
        super(message);
    }

    public NoExistOAuthClientException() {
        super("제공되지 않는 소셜 로그인 제공처입니다.");
    }
}
