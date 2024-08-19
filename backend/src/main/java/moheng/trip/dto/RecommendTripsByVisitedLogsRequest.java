package moheng.trip.dto;

import java.util.Map;

public class RecommendTripsByVisitedLogsRequest {
    private Map<Long, Long> preferredLocations;
    private Long page;

    private RecommendTripsByVisitedLogsRequest() {
    }

    public RecommendTripsByVisitedLogsRequest(final Map<Long, Long> preferredLocations, final Long page) {
        this.preferredLocations = preferredLocations;
        this.page = page;
    }

    public Map<Long, Long> getPreferredLocations() {
        return preferredLocations;
    }

    public Long getPage() {
        return page;
    }
}
