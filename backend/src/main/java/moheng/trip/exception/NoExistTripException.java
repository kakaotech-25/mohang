package moheng.trip.exception;

public class NoExistTripException extends RuntimeException {
    public NoExistTripException(final String message) {
       super(message);
    }

    public NoExistTripException() {
        super("존재하지 않는 여행지입니다.");
    }
}
