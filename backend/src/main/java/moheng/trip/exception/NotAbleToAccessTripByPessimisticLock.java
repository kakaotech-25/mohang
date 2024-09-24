package moheng.trip.exception;

public class NotAbleToAccessTripByPessimisticLock extends RuntimeException {
    public NotAbleToAccessTripByPessimisticLock(final String message) {
        super(message);
    }
}
