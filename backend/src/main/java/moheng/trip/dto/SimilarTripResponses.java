package moheng.trip.dto;

import moheng.trip.domain.Trip;

import java.util.List;
import java.util.stream.Collectors;

public class SimilarTripResponses {
    private List<FindTripResponse> findTripResponses;

    private SimilarTripResponses() {
    }

    public SimilarTripResponses(List<Trip> trips) {
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
