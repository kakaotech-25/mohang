package moheng.trip.domain.model;

import moheng.trip.dto.RecommendTripsByVisitedLogsRequest;
import moheng.trip.dto.RecommendTripsByVisitedLogsResponse;

public interface ExternalRecommendModelClient {
    RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogs(final RecommendTripsByVisitedLogsRequest request);
}
