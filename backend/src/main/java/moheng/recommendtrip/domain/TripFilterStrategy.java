package moheng.recommendtrip.domain;

import moheng.trip.domain.Trip;

import java.util.List;

public interface TripFilterStrategy {
    boolean isMatch(final String strategyName);
    List<Trip> execute();
}
