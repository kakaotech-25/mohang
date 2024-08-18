package moheng.recommendtrip.exception;

public class NoExistMemberTripException extends RuntimeException {
    public NoExistMemberTripException(final String message) {
        super(message);
    }

    public NoExistMemberTripException() {
        super("존재하지 않는 멤버의 여행지입니다.");
    }
}
