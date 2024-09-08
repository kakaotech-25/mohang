package moheng.keyword.dto;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FindTripsWithRandomKeywordResponse {
    private String keywordName;
    private List<FindTripResponse> findTripResponses;

    private FindTripsWithRandomKeywordResponse() {
    }

    public FindTripsWithRandomKeywordResponse(final List<TripKeyword> tripKeywords, final Keyword keyword) {
        this.keywordName = keyword.getName();
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

    public String getKeywordName() {
        return keywordName;
    }

    public List<FindTripResponse> getFindTripResponses() {
        return findTripResponses;
    }
}
