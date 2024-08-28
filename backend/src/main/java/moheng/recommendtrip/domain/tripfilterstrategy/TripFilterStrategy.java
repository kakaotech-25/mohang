package moheng.recommendtrip.domain.tripfilterstrategy;

import moheng.trip.domain.Trip;

import java.util.List;

public interface TripFilterStrategy {
    boolean isMatch(final String strategyName);
    List<Trip> execute(final long memberId);
}
