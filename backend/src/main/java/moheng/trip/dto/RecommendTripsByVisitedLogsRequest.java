package moheng.trip.dto;

import java.util.Map;

public class RecommendTripsByVisitedLogsRequest {
    private Map<Long, Long> preferredLocation;
    private Long page;

    private RecommendTripsByVisitedLogsRequest() {
    }

    public RecommendTripsByVisitedLogsRequest(final Map<Long, Long> preferredLocation, final Long page) {
        this.preferredLocation = preferredLocation;
        this.page = page;
    }

    public Map<Long, Long> getPreferredLocations() {
        return preferredLocation;
    }

    public Long getPage() {
        return page;
    }
}
