package moheng.planner.exception;

import org.hibernate.boot.archive.scan.internal.ScanResultImpl;

public class NoExistTripScheduleRegistryException extends RuntimeException {
    public NoExistTripScheduleRegistryException(final String message) {
        super(message);
    }
}
