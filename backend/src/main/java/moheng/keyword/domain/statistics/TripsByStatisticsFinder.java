package moheng.keyword.domain.statistics;

import moheng.keyword.domain.TripKeyword;
import moheng.trip.domain.Trip;

import java.util.List;

public interface TripsByStatisticsFinder {
    List<Trip> findTripsWithVisitedCount(final List<TripKeyword> tripKeywords);
}
