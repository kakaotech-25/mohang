package moheng.trip.application;

import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsOrderByVisitedCountDescResponse;
import moheng.trip.dto.TripCreateRequest;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.repository.TripRepository;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip findByContentId(final Long contentId) {
        final Trip trip = tripRepository.findByContentId(contentId)
                .orElseThrow(NoExistTripException::new);
        return trip;
    }

    public Trip findById(final Long tripId) {
        final Trip trip = tripRepository.findById(tripId)
                .orElseThrow(NoExistTripException::new);
        return trip;
    }

    public void createTrip(final TripCreateRequest tripCreateRequest) {
        final Trip trip = new Trip(
                tripCreateRequest.getName(),
                tripCreateRequest.getPlaceName(),
                tripCreateRequest.getContentId(),
                tripCreateRequest.getDescription(),
                tripCreateRequest.getTripImageUrl()
        );
        tripRepository.save(trip);
    }

    public void save(final Trip trip) {
        tripRepository.save(trip);
    }

    public FindTripsOrderByVisitedCountDescResponse findTop30OrderByVisitedCountDesc() {
        return new FindTripsOrderByVisitedCountDescResponse(tripRepository.findTop30ByOrderByVisitedCountDesc());
    }
}
