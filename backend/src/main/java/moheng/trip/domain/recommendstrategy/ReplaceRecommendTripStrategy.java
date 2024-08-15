package moheng.trip.domain.recommendstrategy;

import moheng.member.domain.Member;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.exception.NoExistRecommendTripException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReplaceRecommendTripStrategy implements RecommendTripStrategy {
    private static final long LOWEST_PRIORITY_RANK = 10L;
    private static final long HIGHEST_PRIORITY_RANK = 1L;
    private final RecommendTripRepository recommendTripRepository;

    public ReplaceRecommendTripStrategy(final RecommendTripRepository recommendTripRepository) {
        this.recommendTripRepository = recommendTripRepository;
    }

    @Override
    public void execute(Trip trip, Member member, List<RecommendTrip> recommendTrips) {
        if (!recommendTripRepository.existsByMemberAndTrip(member, trip)) {
            downAllRanks(recommendTrips);
            deleteHighestPriorityRankRecommendTrip(member);
            recommendTripRepository.save(new RecommendTrip(trip, member, LOWEST_PRIORITY_RANK));
        }
    }

    private void deleteHighestPriorityRankRecommendTrip(final Member member) {
        if(!recommendTripRepository.existsByMemberAndRank(member, HIGHEST_PRIORITY_RANK - 1)) {
            throw new NoExistRecommendTripException("존재하지 않는 선호 여행지 정보입니다.");
        }
        recommendTripRepository.deleteByMemberAndRank(member, HIGHEST_PRIORITY_RANK - 1);
    }

    private void downAllRanks(List<RecommendTrip> recommendTrips) {
        recommendTripRepository.bulkDownRank(recommendTrips);
    }

    @Override
    public boolean isMatch(long recommendSize) {
        return false;
    }
}
