package moheng.recommendtrip.domain;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.exception.LackOfRecommendTripException;
import moheng.recommendtrip.exception.NoExistMemberTripException;
import moheng.trip.domain.MemberTrip;
import moheng.trip.domain.MemberTripRepository;
import moheng.trip.domain.Trip;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
@Component
public class PreferredLocationsProvider {
    private static final int MIN_RECOMMEND_TRIPS_COUNT = 5;
    private final MemberRepository memberRepository;
    private final RecommendTripRepository recommendTripRepository;
    private final MemberTripRepository memberTripRepository;

    public PreferredLocationsProvider(final MemberRepository memberRepository,
                                      final RecommendTripRepository recommendTripRepository,
                                      final MemberTripRepository memberTripRepository) {
        this.memberRepository = memberRepository;
        this.recommendTripRepository = recommendTripRepository;
        this.memberTripRepository = memberTripRepository;
    }

    public Map<Long, Long> findPreferredLocations(final long memberId) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NoExistMemberException::new);
        final List<MemberTrip> memberTrips = memberTripRepository.findByMember(member);
        final List<RecommendTrip> recommendTrips = recommendTripRepository.findByMemberOrderByRankDesc(member);
        return findMemberPreferredLocations(memberTrips, recommendTrips);
    }

    private Map<Long, Long> findMemberPreferredLocations(final List<MemberTrip> memberTrips, final List<RecommendTrip> recommendTrips) {
        validateRecommendTrips(recommendTrips);
        final Map<Long, Long> preferredLocations = new HashMap<>();
        for (final RecommendTrip recommendTrip : recommendTrips) {
            final Trip trip = recommendTrip.getTrip();
            final MemberTrip memberTrip = findMemberTripByContentId(memberTrips, trip);
            preferredLocations.put(trip.getContentId(), memberTrip.getVisitedCount());
        }
        return preferredLocations;
    }

    private MemberTrip findMemberTripByContentId(final List<MemberTrip> memberTrips, final Trip trip) {
        return memberTrips.stream()
                .filter(mt -> mt.getTrip().getContentId().equals(trip.getContentId()))
                .findFirst()
                .orElseThrow(() -> new NoExistMemberTripException("존재하지 않는 멤버의 선호 여행지입니다."));
    }

    private void validateRecommendTrips(final List<RecommendTrip> recommendTrips) {
        if(recommendTrips.size() < MIN_RECOMMEND_TRIPS_COUNT) {
            throw new LackOfRecommendTripException("추천을 받기위한 선호 여행지 데이터 수가 부족합니다.");
        }
    }
}
