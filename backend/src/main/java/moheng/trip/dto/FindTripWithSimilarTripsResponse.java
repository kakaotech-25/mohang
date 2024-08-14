package moheng.trip.dto;

public class FindTripWithSimilarTripsResponse {
    private FindTripResponse findTripResponse;
    private SimilarTripResponses similarTripResponses;

    private FindTripWithSimilarTripsResponse() {
    }

    public FindTripWithSimilarTripsResponse(final FindTripResponse findTripResponse, final SimilarTripResponses similarTripResponses) {
        this.findTripResponse = findTripResponse;
        this.similarTripResponses = similarTripResponses;
    }

    public FindTripResponse getFindTripResponse() {
        return findTripResponse;
    }

    public SimilarTripResponses getSimilarTripResponses() {
        return similarTripResponses;
    }
}
