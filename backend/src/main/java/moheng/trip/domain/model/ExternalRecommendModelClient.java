package moheng.trip.domain.model;

import moheng.trip.dto.request.RecommendTripsByVisitedLogsRequest;
import moheng.trip.dto.response.RecommendTripsByVisitedLogsResponse;

public interface ExternalRecommendModelClient {
    RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogs(final RecommendTripsByVisitedLogsRequest request);
}
