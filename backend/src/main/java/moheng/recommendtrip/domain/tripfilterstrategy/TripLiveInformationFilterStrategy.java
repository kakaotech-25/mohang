package moheng.recommendtrip.domain.tripfilterstrategy;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.recommendtrip.domain.filterinfo.FilterStandardInfo;
import moheng.trip.domain.ExternalSimilarTripModelClient;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import moheng.trip.dto.FindSimilarTripWithContentIdResponses;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TripLiveInformationFilterStrategy implements TripFilterStrategy {
    private static final String STRATEGY_NAME = "LIVE_INFO";
    private static final int SIMILAR_TRIPS_COUNT = 10;
    private final ExternalSimilarTripModelClient externalSimilarTripModelClient;
    private final LiveInformationRepository liveInformationRepository;
    private final TripRepository tripRepository;

    public TripLiveInformationFilterStrategy(final ExternalSimilarTripModelClient externalSimilarTripModelClient,
                                             final LiveInformationRepository liveInformationRepository,
                                             final TripRepository tripRepository) {
        this.externalSimilarTripModelClient = externalSimilarTripModelClient;
        this.liveInformationRepository = liveInformationRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public boolean isMatch(final String strategyName) {
        return STRATEGY_NAME.equals(strategyName);
    }

    @Override
    public List<Trip> execute(final FilterStandardInfo filterInfo) {
        final Trip trip = tripRepository.findById(filterInfo.getInfo())
                .orElseThrow(NoExistTripException::new);

        return findFilteredSimilarTripsByLiveInformation(trip);
    }

    private List<Trip> findFilteredSimilarTripsByLiveInformation(final Trip trip) {
        final List<Trip> filteredSimilarTrips = new ArrayList<>();
        long page = 1L;
        while(filteredSimilarTrips.size() < SIMILAR_TRIPS_COUNT) {
            final FindSimilarTripWithContentIdResponses similarTripWithContentIdResponses = externalSimilarTripModelClient.findSimilarTrips(trip.getContentId(), page);
            final List<Trip> filteredTripsByLiveInformation = findFilteredTripsByLiveInformation(trip, similarTripWithContentIdResponses.getContentIds());
            filteredSimilarTrips.addAll(filteredTripsByLiveInformation);
            page++;
        }
        if(filteredSimilarTrips.size() > SIMILAR_TRIPS_COUNT) {
            return filteredSimilarTrips.subList(0, SIMILAR_TRIPS_COUNT);
        }
        return filteredSimilarTrips;
    }

    private List<Trip> findFilteredTripsByLiveInformation(final Trip currentTrip, final List<Long> contentIds) {
        final LiveInformation liveInformation = liveInformationRepository.findLiveInformationByTrip(currentTrip);
        return tripRepository.findFilteredTripsByLiveInformation(liveInformation.getId(), contentIds);
    }
}
