package moheng.keyword.domain;

import moheng.trip.domain.Trip;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TripsByVisitedCountFinder implements TripsByStatisticsFinder {

    public List<Trip> findTripsWithVisitedCount(final List<TripKeyword> tripKeywords) {
        final Map<Trip, Long> tripClickCounts = new HashMap<>();
        for (TripKeyword tripKeyword : tripKeywords) {
            final Trip trip = tripKeyword.getTrip();
            tripClickCounts.put(tripKeyword.getTrip(), trip.getVisitedCount());
        }
        return findSortedTripsByVisitedCount(tripClickCounts);
    }

    private List<Trip> findSortedTripsByVisitedCount(final Map<Trip, Long> tripsWithVisitedCount) {
        return tripsWithVisitedCount.entrySet().stream()
                .sorted((visitedCnt1, visitedCnt2) -> visitedCnt2.getValue().compareTo(visitedCnt1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
