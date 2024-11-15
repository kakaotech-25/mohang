package moheng.planner.dto.response;

import moheng.planner.domain.TripSchedule;

import java.util.List;
import java.util.stream.Collectors;

public class FindPlannerPublicForCreatedAtRangeResponses {
    private List<TripScheduleRangeResponse> tripScheduleResponses;

    private FindPlannerPublicForCreatedAtRangeResponses() {
    }

    public FindPlannerPublicForCreatedAtRangeResponses(final List<TripSchedule> tripSchedules) {
        this.tripScheduleResponses = toResponses(tripSchedules);
    }

    private List<TripScheduleRangeResponse> toResponses(final List<TripSchedule> tripSchedules) {
        return tripSchedules.stream()
                .map(TripScheduleRangeResponse::new)
                .collect(Collectors.toList());
    }

    public List<TripScheduleRangeResponse> getTripScheduleResponses() {
        return tripScheduleResponses;
    }
}
