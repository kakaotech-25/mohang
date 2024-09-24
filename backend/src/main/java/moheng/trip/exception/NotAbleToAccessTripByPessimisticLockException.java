package moheng.trip.exception;

public class NotAbleToAccessTripByPessimisticLockException extends RuntimeException {
    public NotAbleToAccessTripByPessimisticLockException(final String message) {
        super(message);
    }
}
