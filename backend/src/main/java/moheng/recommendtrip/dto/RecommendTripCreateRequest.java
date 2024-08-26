package moheng.recommendtrip.dto;

public class RecommendTripCreateRequest {
    private Long tripId;

    private RecommendTripCreateRequest() {
    }

    public RecommendTripCreateRequest(final Long tripId) {
        this.tripId = tripId;
    }

    public Long getTripId() {
        return tripId;
    }
}
