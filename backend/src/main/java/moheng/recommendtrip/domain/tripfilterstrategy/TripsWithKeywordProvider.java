package moheng.recommendtrip.domain.tripfilterstrategy;

import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.trip.domain.Trip;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TripsWithKeywordProvider {
    private final TripKeywordRepository tripKeywordRepository;

    public TripsWithKeywordProvider(final TripKeywordRepository tripKeywordRepository) {
        this.tripKeywordRepository = tripKeywordRepository;
    }

    public List<TripKeyword> findWithKeywords(final List<Trip> trips) {
        return tripKeywordRepository.findByTrips(trips);
    }
}
