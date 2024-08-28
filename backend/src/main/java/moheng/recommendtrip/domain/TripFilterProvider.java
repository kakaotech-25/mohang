package moheng.recommendtrip.domain;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.MemberLiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.trip.domain.ExternalRecommendModelClient;
import moheng.trip.domain.ExternalSimilarTripModelClient;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import moheng.trip.dto.RecommendTripsByVisitedLogsRequest;
import moheng.trip.dto.RecommendTripsByVisitedLogsResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TripFilterProvider {
    private static final int RECOMMEND_TRIPS_COUNT = 10;
    private final ExternalRecommendModelClient externalRecommendModelClient;
    private final ExternalSimilarTripModelClient externalSimilarTripModelClient;
    private final TripRepository tripRepository;
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final TripLiveInformationRepository tripLiveInformationRepository;

    public TripFilterProvider(final ExternalRecommendModelClient externalRecommendModelClient,
                              final ExternalSimilarTripModelClient externalSimilarTripModelClient,
                              final TripRepository tripRepository,
                              final MemberLiveInformationRepository memberLiveInformationRepository,
                              final TripLiveInformationRepository tripLiveInformationRepository) {
        this.externalRecommendModelClient = externalRecommendModelClient;
        this.externalSimilarTripModelClient = externalSimilarTripModelClient;
        this.tripRepository = tripRepository;
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.tripLiveInformationRepository = tripLiveInformationRepository;
    }

    public List<Trip> findFilteredTrips(final Map<Long, Long> preferredLocations, final long memberId) {
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
        return filterTripsByMemberInformation(trips, memberLiveInformations);
    }

    private List<Trip> filterTripsByMemberInformation(final List<Trip> trips, final List<LiveInformation> memberLiveInformations) {
        return trips.stream()
                .filter(trip -> tripLiveInformationRepository.existsByTripAndLiveInformationIn(trip, memberLiveInformations))
                .collect(Collectors.toList());
    }
}
