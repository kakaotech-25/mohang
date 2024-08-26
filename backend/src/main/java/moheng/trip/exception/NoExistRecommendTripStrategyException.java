package moheng.trip.exception;

public class NoExistRecommendTripStrategyException extends RuntimeException {
    public NoExistRecommendTripStrategyException(final String message) {
        super(message);
    }

    public NoExistRecommendTripStrategyException() {
        super("제공되지 않는 선호 여행지 저장 전략입니다.");
    }
}
