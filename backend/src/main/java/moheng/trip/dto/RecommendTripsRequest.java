package moheng.trip.dto;

import java.util.Map;

public class RecommendTripsRequest {
    private Map<Long, Long> preferredLocation;

    private RecommendTripsRequest() {
    }

    public RecommendTripsRequest(final Map<Long, Long> preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public Map<Long, Long> getPreferredLocation() {
        return preferredLocation;
    }
}
