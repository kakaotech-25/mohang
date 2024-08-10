package moheng.trip.application;

import moheng.trip.domain.Trip;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.repository.TripRepository;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip findById(final Long tripId) {
        final Trip trip = tripRepository.findById(tripId)
                .orElseThrow(NoExistTripException::new);
        return trip;
    }

    public void save(final Trip trip) {
        tripRepository.save(trip);
    }
}
