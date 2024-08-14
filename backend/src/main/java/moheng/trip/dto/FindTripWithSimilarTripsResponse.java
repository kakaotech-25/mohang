package moheng.trip.dto;

import moheng.trip.domain.Trip;

public class FindTripWithSimilarTripsResponse {
    private FindTripResponse findTripResponse;
    private SimilarTripResponses similarTripResponses;

    private FindTripWithSimilarTripsResponse() {
    }

    public FindTripWithSimilarTripsResponse(final Trip trip, final SimilarTripResponses similarTripResponses) {
        this.findTripResponse = toResponse(trip);
        this.similarTripResponses = similarTripResponses;
    }

    private FindTripResponse toResponse(Trip trip) {
        return new FindTripResponse(trip);
    }

    public FindTripResponse getFindTripResponse() {
        return findTripResponse;
    }

    public SimilarTripResponses getSimilarTripResponses() {
        return similarTripResponses;
    }
}
