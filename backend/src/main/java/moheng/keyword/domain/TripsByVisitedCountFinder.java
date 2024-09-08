package moheng.keyword.domain;

import moheng.trip.domain.Trip;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TripsByVisitedCountFinder implements TripsByStatisticsFinder {
    private static final int TOP_TRIPS_COUNT = 30;

    public List<Trip> findTripsWithVisitedCount(final List<TripKeyword> tripKeywords) {
        final Map<Trip, Long> tripClickCounts = new HashMap<>();
        for (TripKeyword tripKeyword : tripKeywords) {
            final Trip trip = tripKeyword.getTrip();
            tripClickCounts.put(tripKeyword.getTrip(), trip.getVisitedCount());
        }
        return findTopTripsByVisitedCount(tripClickCounts);
    }

    private List<Trip> findTopTripsByVisitedCount(final Map<Trip, Long> tripsWithVisitedCount) {
        return tripsWithVisitedCount.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(TOP_TRIPS_COUNT)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
