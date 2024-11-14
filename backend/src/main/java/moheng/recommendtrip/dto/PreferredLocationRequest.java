package moheng.recommendtrip.dto;

import moheng.trip.dto.request.RecommendTripsByVisitedLogsRequest;

import java.util.List;

public class PreferredLocationRequest {
    private List<RecommendTripsByVisitedLogsRequest.LocationPreference> preferredLocation;

    private PreferredLocationRequest() {
    }

    public PreferredLocationRequest(List<RecommendTripsByVisitedLogsRequest.LocationPreference> preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public List<RecommendTripsByVisitedLogsRequest.LocationPreference> getPreferredLocation() {
        return preferredLocation;
    }
}
