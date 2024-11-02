package moheng.liveinformation.exception;

public class NoExistLiveInformationException extends RuntimeException {
    public NoExistLiveInformationException(final String message) {
        super(message);
    }

    public NoExistLiveInformationException() {
       super("존재하지 않는 생활정보입니다.");
    }
}
