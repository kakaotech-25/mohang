package moheng.recommendtrip.application;

import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.tripfilterstrategy.TripFilterStrategyProvider;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.domain.*;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.exception.NoExistTripException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class RecommendTripService {
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
