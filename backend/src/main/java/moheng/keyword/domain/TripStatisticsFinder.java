package moheng.keyword.domain;

import moheng.trip.domain.Trip;

import java.util.List;
import java.util.Map;

public interface TripStatisticsFinder {
    Map<Trip, Long> findTripsWithVisitedCount(final List<TripKeyword> tripKeywords);
}
