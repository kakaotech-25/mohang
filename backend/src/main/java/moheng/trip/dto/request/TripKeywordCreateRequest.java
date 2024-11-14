package moheng.trip.dto.request;

public class TripKeywordCreateRequest {
    private Long tripId;
    private Long keywordId;

    private TripKeywordCreateRequest() {
    }

    public TripKeywordCreateRequest(final Long tripId, final Long keywordId) {
        this.tripId = tripId;
        this.keywordId = keywordId;
    }

    public Long getTripId() {
        return tripId;
    }

    public Long getKeywordId() {
        return keywordId;
    }
}
