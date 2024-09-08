package moheng.keyword.domain;

import moheng.trip.domain.Trip;

import java.util.List;

public interface TripsByStatisticsFinder {
    List<Trip> findTripsWithVisitedCount(final List<TripKeyword> tripKeywords);
}
