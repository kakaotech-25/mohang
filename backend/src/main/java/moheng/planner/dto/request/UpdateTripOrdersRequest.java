package moheng.planner.dto.request;

import java.util.List;

public class UpdateTripOrdersRequest {
    private List<Long> tripIds;

    private UpdateTripOrdersRequest() {
    }

    public UpdateTripOrdersRequest(final List<Long> tripIds) {
        this.tripIds = tripIds;
    }

    public List<Long> getTripIds() {
        return tripIds;
    }
}
