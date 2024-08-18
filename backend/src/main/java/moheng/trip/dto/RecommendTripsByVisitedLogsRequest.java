package moheng.trip.dto;

import java.util.Map;

public class RecommendTripsByVisitedLogsRequest {
    private Map<Long, Long> preferredLocations;

    private RecommendTripsByVisitedLogsRequest() {
    }

    public RecommendTripsByVisitedLogsRequest(final Map<Long, Long> preferredLocations) {
        this.preferredLocations = preferredLocations;
    }

    public Map<Long, Long> getPreferredLocations() {
        return preferredLocations;
    }
}
