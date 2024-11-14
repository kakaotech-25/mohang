package moheng.config.stub;

import moheng.trip.domain.model.ExternalRecommendModelClient;
import moheng.trip.dto.request.RecommendTripsByVisitedLogsRequest;
import moheng.trip.dto.response.RecommendTripsByVisitedLogsResponse;

import java.util.List;

public class StubRecommendTripModelClient implements ExternalRecommendModelClient {
    private static final List<Long> contentIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);

    @Override
    public RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogs(RecommendTripsByVisitedLogsRequest request) {
        return new RecommendTripsByVisitedLogsResponse(contentIds);
    }
}
