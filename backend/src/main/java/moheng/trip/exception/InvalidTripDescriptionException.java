package moheng.trip.exception;

public class InvalidTripDescriptionException extends RuntimeException {
    public InvalidTripDescriptionException(final String message) {
        super(message);
    }
}
