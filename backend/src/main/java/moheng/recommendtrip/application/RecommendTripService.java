package moheng.recommendtrip.application;

import moheng.keyword.domain.TripKeywordRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.filterinfo.PreferredLocationsFilterInfo;
import moheng.recommendtrip.domain.tripfilterstrategy.TripFilterStrategy;
import moheng.recommendtrip.domain.tripfilterstrategy.TripFilterStrategyProvider;
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
    private final TripKeywordRepository tripKeywordRepository;

    public RecommendTripService(final TripFilterStrategyProvider tripFilterStrategyProvider,
                                final RecommendTripRepository recommendTripRepository,
                                final MemberRepository memberRepository,
                                final TripRepository tripRepository,
                                final TripKeywordRepository tripKeywordRepository) {
        this.tripFilterStrategyProvider = tripFilterStrategyProvider;
        this.recommendTripRepository = recommendTripRepository;
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
    }

    public FindTripsResponse findRecommendTripsByModel(final long memberId) {
        final TripFilterStrategy tripFilterStrategy = tripFilterStrategyProvider.findTripsByFilterStrategy(PREFERRED_LOCATIONS_STRATEGY);
        final List<Trip> filteredTrips = tripFilterStrategy.execute(new PreferredLocationsFilterInfo(memberId));
        return new FindTripsResponse(tripKeywordRepository.findByTrips(filteredTrips));
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
