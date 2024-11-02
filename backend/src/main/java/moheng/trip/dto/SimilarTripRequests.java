package moheng.trip.dto;

public class SimilarTripRequests {
    private Long contentId;
    private Long page;

    private SimilarTripRequests() {
    }

    public SimilarTripRequests(final Long contentId, final Long page) {
        this.contentId = contentId;
        this.page = page;
    }

    public Long getContentId() {
        return contentId;
    }

    public Long getPage() {
        return page;
    }
}
