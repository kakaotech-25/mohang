package moheng.planner.dto.request;

import moheng.planner.domain.TripSchedule;
import moheng.planner.dto.response.TripScheduleResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FindPublicSchedulesForCurrentMonthResponses {
    private List<TripScheduleResponse> tripScheduleResponses;

    private FindPublicSchedulesForCurrentMonthResponses() {
    }

    public FindPublicSchedulesForCurrentMonthResponses(final List<TripSchedule> tripSchedules) {
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
