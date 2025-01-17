package moheng.planner.dto.response;

import moheng.planner.domain.TripSchedule;

import java.util.List;
import java.util.stream.Collectors;

public class FindPlannerOrderByRecentResponse {
    private List<TripScheduleResponse> tripScheduleResponses;

    private FindPlannerOrderByRecentResponse() {
    }

    public FindPlannerOrderByRecentResponse(final List<TripSchedule> tripSchedules) {
        this.tripScheduleResponses = toResponses(tripSchedules);
    }

    private List<TripScheduleResponse> toResponses(final List<TripSchedule> tripSchedules) {
        return tripSchedules.stream()
                .map(TripScheduleResponse::new)
                .collect(Collectors.toList());
    }

    public List<TripScheduleResponse> getTripScheduleResponses() {
        return tripScheduleResponses;
    }
}
