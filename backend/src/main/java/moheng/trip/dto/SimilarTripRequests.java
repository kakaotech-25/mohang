package moheng.trip.dto;

public class SimilarTripRequests {
    private Long contentId;

    private SimilarTripRequests() {
    }

    public SimilarTripRequests(final Long contentId) {
        this.contentId = contentId;
    }

    public Long getContentId() {
        return contentId;
    }
}
