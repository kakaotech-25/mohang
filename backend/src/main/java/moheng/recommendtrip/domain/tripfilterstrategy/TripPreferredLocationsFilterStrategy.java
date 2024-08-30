package moheng.recommendtrip.domain.tripfilterstrategy;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.MemberLiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.recommendtrip.domain.PreferredLocationsProvider;
import moheng.recommendtrip.domain.filterinfo.FilterStandardInfo;
import moheng.trip.domain.ExternalRecommendModelClient;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import moheng.trip.dto.RecommendTripsByVisitedLogsRequest;
import moheng.trip.dto.RecommendTripsByVisitedLogsResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TripPreferredLocationsFilterStrategy implements TripFilterStrategy {
    private static final String STRATEGY_NAME = "PREFERRED_LOCATIONS";
    private static final int RECOMMEND_TRIPS_COUNT = 10;
    private final PreferredLocationsProvider preferredLocationsProvider;
    private final ExternalRecommendModelClient externalRecommendModelClient;
    private final TripRepository tripRepository;
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final TripLiveInformationRepository tripLiveInformationRepository;

    public TripPreferredLocationsFilterStrategy(final PreferredLocationsProvider preferredLocationsProvider,
                                             final ExternalRecommendModelClient externalRecommendModelClient,
                                             final TripRepository tripRepository,
                                             final MemberLiveInformationRepository memberLiveInformationRepository,
                                             final TripLiveInformationRepository tripLiveInformationRepository) {
        this.preferredLocationsProvider = preferredLocationsProvider;
        this.externalRecommendModelClient = externalRecommendModelClient;
        this.tripRepository = tripRepository;
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.tripLiveInformationRepository = tripLiveInformationRepository;
    }

    @Override
    public boolean isMatch(final String strategyName) {
        return STRATEGY_NAME.equals(strategyName);
    }

    @Override
    public List<Trip> execute(final FilterStandardInfo filterStandardInfo) {
        final Map<Long, Long> preferredLocations = preferredLocationsProvider.findPreferredLocations(filterStandardInfo.getInfo());
        return findFilteredTripsWithPreferredLocations(preferredLocations, filterStandardInfo.getInfo());
    }

    public List<Trip> findFilteredTripsWithPreferredLocations(final Map<Long, Long> preferredLocations, final long memberId) {
        final List<Trip> filteredTrips = new ArrayList<>();
        long page = 1L;
        while (filteredTrips.size() < RECOMMEND_TRIPS_COUNT) {
            final List<Trip> filteredTripsByLiveinformation = findRecommendTripsByModelClient(preferredLocations, memberId, page);
            filteredTrips.addAll(filteredTripsByLiveinformation);
            page++;
        }
        if (filteredTrips.size() > RECOMMEND_TRIPS_COUNT) {
            return filteredTrips.subList(0, RECOMMEND_TRIPS_COUNT);
        }
        return filteredTrips;
    }

    private List<Trip> findRecommendTripsByModelClient(final Map<Long, Long> preferredLocations, final long memberId, final long page) {
        final RecommendTripsByVisitedLogsResponse response = externalRecommendModelClient.recommendTripsByVisitedLogs(
                new RecommendTripsByVisitedLogsRequest(preferredLocations, page)
        );
        return filterTripsByLiveinformation(response, memberId);
    }

    private List<Trip> filterTripsByLiveinformation(final RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogsResponse, final Long memberId) {
        final List<Trip> trips = tripRepository.findTripsByContentIds(recommendTripsByVisitedLogsResponse.getContentIds());
        final List<LiveInformation> memberLiveInformations = memberLiveInformationRepository.findLiveInformationsByMemberId(memberId);

        if (memberLiveInformations == null || memberLiveInformations.isEmpty()) {
            return trips;
        }
        return filterTripsByMemberInformation(trips, memberLiveInformations);
    }


    private List<Trip> filterTripsByMemberInformation(final List<Trip> trips, final List<LiveInformation> memberLiveInformations) {
        return trips.stream()
                .filter(trip -> tripLiveInformationRepository.existsByTripAndLiveInformationIn(trip, memberLiveInformations))
                .collect(Collectors.toList());
    }
}
