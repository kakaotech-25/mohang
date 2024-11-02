package moheng.trip.exception;

public class InvalidTripNameException extends RuntimeException{
    public InvalidTripNameException(final String message) {
        super(message);
    }
}
