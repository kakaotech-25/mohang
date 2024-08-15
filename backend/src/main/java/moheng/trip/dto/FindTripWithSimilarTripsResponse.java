package moheng.trip.dto;

import moheng.trip.domain.Trip;

import java.util.List;

public class FindTripWithSimilarTripsResponse {
    private FindTripResponse findTripResponse;
    private SimilarTripResponses similarTripResponses;

    private FindTripWithSimilarTripsResponse() {
    }

    public FindTripWithSimilarTripsResponse(final Trip trip, final List<String> keywords, final SimilarTripResponses similarTripResponses) {
        this.findTripResponse = new FindTripResponse(trip, keywords);
        this.similarTripResponses = similarTripResponses;
    }

    public FindTripResponse getFindTripResponse() {
        return findTripResponse;
    }

    public SimilarTripResponses getSimilarTripResponses() {
        return similarTripResponses;
    }
}

