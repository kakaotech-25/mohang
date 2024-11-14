package moheng.planner.dto.response;

import moheng.planner.domain.TripSchedule;
import moheng.trip.domain.Trip;

import java.util.List;
import java.util.stream.Collectors;

public class FindTripsOnSchedule {
    private TripScheduleResponse tripScheduleResponse;
    private List<FindTripOnSchedule> findTripsOnSchedules;

    private FindTripsOnSchedule() {
    }

    public FindTripsOnSchedule(final TripSchedule tripSchedule, final List<Trip> trips) {
        this.tripScheduleResponse = toResponse(tripSchedule);
        this.findTripsOnSchedules = toResponses(trips);
    }

    private TripScheduleResponse toResponse(final TripSchedule tripSchedule) {
        return new TripScheduleResponse(tripSchedule);
    }

    private List<FindTripOnSchedule> toResponses(final List<Trip> trips) {
        return trips.stream()
                .map(FindTripOnSchedule::new)
                .collect(Collectors.toList());
    }

    public TripScheduleResponse getTripScheduleResponse() {
        return tripScheduleResponse;
    }

    public List<FindTripOnSchedule> getFindTripsOnSchedules() {
        return findTripsOnSchedules;
    }
}
