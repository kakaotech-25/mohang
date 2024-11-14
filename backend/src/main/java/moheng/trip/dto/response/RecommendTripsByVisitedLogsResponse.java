package moheng.trip.dto.response;

import java.util.List;

public class RecommendTripsByVisitedLogsResponse {
    private List<Long> contentIds;

    private RecommendTripsByVisitedLogsResponse() {
    }

    public RecommendTripsByVisitedLogsResponse(final List<Long> contentIds) {
        this.contentIds = contentIds;
    }

    public List<Long> getContentIds() {
        return contentIds;
    }
}
