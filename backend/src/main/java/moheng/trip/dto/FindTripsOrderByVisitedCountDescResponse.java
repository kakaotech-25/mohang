package moheng.trip.dto;

import moheng.liveinformation.dto.LiveInformationResponse;
import moheng.trip.domain.Trip;

import java.util.List;
import java.util.stream.Collectors;

public class FindTripsOrderByVisitedCountDescResponse {
    private List<FindTripResponse> findTripResponses;

    public FindTripsOrderByVisitedCountDescResponse(List<Trip> trips) {
        this.findTripResponses = toResponse(trips);
    }

    private List<FindTripResponse> toResponse(List<Trip> trips) {
        return trips.stream()
                .map(FindTripResponse::new)
                .collect(Collectors.toList());
    }

    public List<FindTripResponse> getFindTripResponses() {
        return findTripResponses;
    }
}
