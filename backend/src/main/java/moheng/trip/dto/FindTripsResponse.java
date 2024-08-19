package moheng.trip.dto;

import moheng.keyword.domain.TripKeyword;
import moheng.trip.domain.Trip;

import java.util.*;
import java.util.stream.Collectors;

public class FindTripsResponse {
    private List<FindTripResponse> findTripResponses;

    private FindTripsResponse() {
    }

    public FindTripsResponse(final List<TripKeyword> tripKeywords) {
        this.findTripResponses = toResponse(tripKeywords);
    }

    private List<FindTripResponse> toResponse(final List<TripKeyword> tripKeywords) {
        Map<Trip, List<String>> tripWithKeywords = new LinkedHashMap<>();
        for (TripKeyword tripKeyword : tripKeywords) {
            final Trip trip = tripKeyword.getTrip();
            final String keywordName = tripKeyword.getKeyword().getName();
            tripWithKeywords.computeIfAbsent(trip, k -> new ArrayList<>()).add(keywordName);
        }
        return tripWithKeywords.entrySet().stream()
                .map(trip -> new FindTripResponse(trip.getKey(), trip.getValue()))
                .collect(Collectors.toList());
    }


    public List<FindTripResponse> getFindTripResponses() {
        return findTripResponses;
    }
}
