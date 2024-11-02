package moheng.trip.domain.recommendstrategy;

import moheng.member.domain.Member;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.exception.NoExistRecommendTripException;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class AppendRecommendTripStrategy implements RecommendTripStrategy {
    private final RecommendTripRepository recommendTripRepository;

    public AppendRecommendTripStrategy(final RecommendTripRepository recommendTripRepository) {
        this.recommendTripRepository = recommendTripRepository;
    }

    @Override
    public void execute(final Trip trip, final Member member, final List<RecommendTrip> recommendTrips) {
        final long highestRank = findHighestRecommendTripRank(recommendTrips);
        recommendTripRepository.save(new RecommendTrip(trip, member, highestRank + 1));
    }

    private long findHighestRecommendTripRank(final List<RecommendTrip> recommendTrips) {
        return recommendTrips.stream()
                .max(Comparator.comparing(RecommendTrip::getRanking))
                .map(RecommendTrip::getRanking)
                .orElseThrow(() -> new NoExistRecommendTripException("선호 여행지 정보가 없습니다."));
    }

    @Override
    public boolean isMatch(final long recommendSize, final long maxSize, final long minSize) {
        return recommendSize < maxSize && recommendSize >= minSize;
    }
}
