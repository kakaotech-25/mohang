package moheng.keyword.dto.response;

public class RecommendTripResponse {
    private Long contentId;
    private Long visitedCount;

    private RecommendTripResponse() {
    }

    public RecommendTripResponse(final Long contentId, final Long visitedCount) {
        this.contentId = contentId;
        this.visitedCount = visitedCount;
    }

    public Long getContentId() {
        return contentId;
    }

    public Long getVisitedCount() {
        return visitedCount;
    }

}
