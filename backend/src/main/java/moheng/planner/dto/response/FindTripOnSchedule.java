package moheng.planner.dto.response;

import moheng.trip.domain.Trip;

public class FindTripOnSchedule {
    private Long tripId;
    private String placeName;
    private Double coordinateX;
    private Double coordinateY;

    private FindTripOnSchedule() {
    }

    public FindTripOnSchedule(final Trip trip) {
        this.tripId = trip.getId();
        this.placeName = trip.getName();
        this.coordinateX = trip.getCoordinateX();
        this.coordinateY = trip.getCoordinateY();
    }

    public Long getTripId() {
        return tripId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Double getCoordinateX() {
        return coordinateX;
    }

    public Double getCoordinateY() {
        return coordinateY;
    }
}
