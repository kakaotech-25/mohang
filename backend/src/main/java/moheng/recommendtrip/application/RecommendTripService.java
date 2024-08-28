package moheng.recommendtrip.application;

import moheng.keyword.domain.TripKeywordRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.TripFilterStrategy;
import moheng.recommendtrip.domain.TripFilterStrategyProvider;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.recommendtrip.exception.LackOfRecommendTripException;
import moheng.recommendtrip.exception.NoExistMemberTripException;
import moheng.trip.domain.*;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(readOnly = true)
@Service
public class RecommendTripService {
    private static final int RECOMMEND_TRIPS_COUNT = 10;
    private static final int MIN_RECOMMEND_TRIPS_COUNT = 5;
    private static final String PREFERRED_LOCATIONS_STRATEGY = "PREFERRED_LOCATIONS";
    private final TripFilterStrategyProvider tripFilterStrategyProvider;
    private final RecommendTripRepository recommendTripRepository;
    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;
    private final MemberTripRepository memberTripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public RecommendTripService(final TripFilterStrategyProvider tripFilterStrategyProvider,
                                final RecommendTripRepository recommendTripRepository,
                                final MemberRepository memberRepository,
                                final TripRepository tripRepository,
                                final MemberTripRepository memberTripRepository,
                                final TripKeywordRepository tripKeywordRepository) {
        this.tripFilterStrategyProvider = tripFilterStrategyProvider;
        this.recommendTripRepository = recommendTripRepository;
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
        this.memberTripRepository = memberTripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
    }

    public FindTripsResponse findRecommendTripsByModel(final long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);
        final List<MemberTrip> memberTrips = memberTripRepository.findByMember(member);
        final List<RecommendTrip> recommendTrips = recommendTripRepository.findTop10ByMember(member);
        final Map<Long, Long> preferredLocations = findMemberPreferredLocations(memberTrips, recommendTrips);
        final TripFilterStrategy tripFilterStrategy = tripFilterStrategyProvider.findFilterTripsByFilterStrategy(PREFERRED_LOCATIONS_STRATEGY);
        final List<Trip> filteredTrips = tripFilterStrategy.execute();
        return new FindTripsResponse(tripKeywordRepository.findByTrips(filteredTrips));
    }

    private Map<Long, Long> findMemberPreferredLocations(final List<MemberTrip> memberTrips, final List<RecommendTrip> recommendTrips) {
        validateRecommendTrips(recommendTrips);
        final Map<Long, Long> preferredLocations = new HashMap<>();
        for (final RecommendTrip recommendTrip : recommendTrips) {
            final Trip trip = recommendTrip.getTrip();
            final MemberTrip memberTrip = memberTrips.stream()
                    .filter(mt -> mt.getTrip().getContentId().equals(trip.getContentId()))
                    .findFirst()
                    .orElseThrow(() -> new NoExistMemberTripException("존재하지 않는 멤버의 선호 여행지입니다."));
            preferredLocations.put(trip.getContentId(), memberTrip.getVisitedCount());
        }
        return preferredLocations;
    }

    private void validateRecommendTrips(final List<RecommendTrip> recommendTrips) {
        if(recommendTrips.size() < MIN_RECOMMEND_TRIPS_COUNT) {
            throw new LackOfRecommendTripException("추천을 받기위한 선호 여행지 데이터 수가 부족합니다.");
        }
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
