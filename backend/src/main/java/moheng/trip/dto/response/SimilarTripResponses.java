package moheng.trip.dto.response;

import moheng.keyword.domain.TripKeyword;
import moheng.trip.domain.Trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimilarTripResponses {
    private List<FindTripResponse> findTripResponses;

    private SimilarTripResponses() {
    }

    public SimilarTripResponses(final List<Trip> trips, final List<TripKeyword> tripKeywords) {
        this.findTripResponses = toResponse(trips, tripKeywords);
    }

    private List<FindTripResponse> toResponse(final List<Trip> trips, final List<TripKeyword> tripKeywords) {
        Map<Trip, List<String>> tripKeywordMap = new HashMap<>();
        for (TripKeyword tripKeyword : tripKeywords) {
            Trip trip = tripKeyword.getTrip();
            String keywordName = tripKeyword.getKeyword().getName();
            tripKeywordMap.computeIfAbsent(trip, k -> new ArrayList<>()).add(keywordName);
        }
        return trips.stream()
                .map(trip -> new FindTripResponse(trip, tripKeywordMap.getOrDefault(trip, new ArrayList<>())))
                .collect(Collectors.toList());
    }

    public List<FindTripResponse> getFindTripResponses() {
        return findTripResponses;
    }
}
