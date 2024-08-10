package moheng.trip.exception;

public class InvalidTripNameException extends RuntimeException{
    public InvalidTripNameException(String message) {
        super(message);
    }
}
