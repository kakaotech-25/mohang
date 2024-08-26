package moheng.recommendtrip.application;

import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.MemberLiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.recommendtrip.exception.LackOfRecommendTripException;
import moheng.recommendtrip.exception.NoExistMemberTripException;
import moheng.trip.domain.*;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.RecommendTripsByVisitedLogsRequest;
import moheng.trip.dto.RecommendTripsByVisitedLogsResponse;
import moheng.trip.exception.NoExistRecommendTripException;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class RecommendTripService {
    private static final int RECOMMEND_TRIPS_COUNT = 10;
    private static final int MIN_RECOMMEND_TRIPS_COUNT = 5;
    private final ExternalRecommendModelClient externalRecommendModelClient;
    private final RecommendTripRepository recommendTripRepository;
    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;
    private final TripLiveInformationRepository tripLiveInformationRepository;
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final MemberTripRepository memberTripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public RecommendTripService(final ExternalRecommendModelClient externalRecommendModelClient,
                                final RecommendTripRepository recommendTripRepository,
                                final MemberRepository memberRepository,
                                final TripRepository tripRepository,
                                final TripLiveInformationRepository tripLiveInformationRepository,
                                final MemberLiveInformationRepository memberLiveInformationRepository,
                                final MemberTripRepository memberTripRepository,
                                final TripKeywordRepository tripKeywordRepository) {
        this.externalRecommendModelClient = externalRecommendModelClient;
        this.recommendTripRepository = recommendTripRepository;
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
        this.tripLiveInformationRepository = tripLiveInformationRepository;
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.memberTripRepository = memberTripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
    }

    public FindTripsResponse findRecommendTripsByModel(final long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);
        final List<MemberTrip> memberTrips = memberTripRepository.findByMember(member);
        final List<RecommendTrip> recommendTrips = recommendTripRepository.findTop10ByMember(member);
        final Map<Long, Long> preferredLocations = findMemberPreferredLocations(memberTrips, recommendTrips);
        final List<Trip> filteredTrips = findFilteredTrips(preferredLocations, memberId);
        return new FindTripsResponse(tripKeywordRepository.findByTrips(filteredTrips));
    }

    private List<Trip> findFilteredTrips(final Map<Long, Long> preferredLocations, final long memberId) {
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

    private Map<Long, Long> findMemberPreferredLocations(final List<MemberTrip> memberTrips, final List<RecommendTrip> recommendTrips) {
        validateRecommendTrips(recommendTrips);
        final Map<Long, Long> preferredLocations = new HashMap<>();
        for (final RecommendTrip recommendTrip : recommendTrips) {
            final Trip trip = recommendTrip.getTrip();
            final MemberTrip memberTrip = memberTrips.stream()
                    .filter(mt -> mt.getTrip().getContentId().equals(trip.getContentId()))
                    .findFirst()
                    .orElseThrow(() -> new NoExistMemberTripException("존재하지 않는 멤버의 여행지입니다."));
            preferredLocations.put(trip.getContentId(), memberTrip.getVisitedCount());
        }
        return preferredLocations;
    }

    private void validateRecommendTrips(final List<RecommendTrip> recommendTrips) {
        if(recommendTrips.size() < MIN_RECOMMEND_TRIPS_COUNT) {
            throw new LackOfRecommendTripException("추천을 받기위해 선호 여행지 데이터 수가 부족합니다.");
        }
    }

    private List<Trip> filterTripsByLiveinformation(final RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogsResponse, final Long memberId) {
        final List<Trip> trips = tripRepository.findTripsByContentIds(recommendTripsByVisitedLogsResponse.getContentIds());
        final List<LiveInformation> memberLiveInformations = memberLiveInformationRepository.findLiveInformationsByMemberId(memberId);
        List<Trip> trips1 = filterTripsByMemberInformation(trips, memberLiveInformations);
        return filterTripsByMemberInformation(trips, memberLiveInformations);
    }

    private List<Trip> filterTripsByMemberInformation(final List<Trip> trips, final List<LiveInformation> memberLiveInformations) {
        return trips.stream()
                .filter(trip -> tripLiveInformationRepository.existsByTripAndLiveInformationIn(trip, memberLiveInformations))
                .collect(Collectors.toList());
    }


    @Transactional
    public void saveByRank(final Trip trip, final Member member, long rank) {
        recommendTripRepository.save(new RecommendTrip(trip, member, rank));
    }

    @Transactional
    public void createRecommendTrip(final long memberId, final RecommendTripCreateRequest request) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);

        final Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(NoExistTripException::new);

        recommendTripRepository.save(new RecommendTrip(trip, member));
    }
}
