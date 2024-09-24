package moheng.recommendtrip.domain.tripfilterstrategy;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.recommendtrip.domain.filterinfo.FilterStandardInfo;
import moheng.trip.domain.model.ExternalSimilarTripModelClient;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.dto.FindSimilarTripWithContentIdResponses;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        while (filteredSimilarTrips.size() < SIMILAR_TRIPS_COUNT) {
            final FindSimilarTripWithContentIdResponses similarTripWithContentIdResponses = externalSimilarTripModelClient.findSimilarTrips(trip.getContentId(), page);
            final List<Trip> filteredTripsByLiveInformation = findFilteredTripsByLiveInformation(trip, similarTripWithContentIdResponses.getContentIds());
            filteredSimilarTrips.addAll(
                    filteredTripsByLiveInformation
                            .stream()
                            .filter(similarTrip -> !similarTrip.getId().equals(trip.getId()))
                            .collect(Collectors.toList()));
            page++;
        }

        if (filteredSimilarTrips.size() > SIMILAR_TRIPS_COUNT) {
            return filteredSimilarTrips.subList(0, SIMILAR_TRIPS_COUNT);
        }
        return filteredSimilarTrips;
    }

    private List<Trip> findFilteredTripsByLiveInformation(final Trip currentTrip, final List<Long> contentIds) {
        final List<LiveInformation> liveInformations = liveInformationRepository.findLiveInformationByTrip(currentTrip);
        final List<Trip> filteredTrips = new ArrayList<>();

        for (LiveInformation liveInformation : liveInformations) {
            final List<Trip> tripsByLiveInfo = tripRepository.findFilteredTripsByLiveInformation(liveInformation.getId(), contentIds);
            filteredTrips.addAll(tripsByLiveInfo);
        }
        return findDistinctTrips(filteredTrips);
    }

    private List<Trip> findDistinctTrips(final List<Trip> filteredTrips) {
        return filteredTrips.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}