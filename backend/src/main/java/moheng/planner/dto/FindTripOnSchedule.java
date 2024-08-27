package moheng.planner.dto;

import moheng.trip.domain.Trip;

public class FindTripOnSchedule {
    private Long tripId;
    private String placeName;

    private FindTripOnSchedule() {
    }

    public FindTripOnSchedule(final Trip trip) {
        this.tripId = trip.getId();
        this.placeName = trip.getPlaceName();
    }

    public Long getTripId() {
        return tripId;
    }

    public String getPlaceName() {
        return placeName;
    }
}
