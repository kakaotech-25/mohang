package moheng.recommendtrip.domain.tripfilterstrategy;

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

// 필터 전략 2가지 종류
// 1. 선호 여행지 (+ 멤버 생활정보)  (-> AI 맞춤 추천 여행지)  / 2. 생활정보 (-> 비슷한 여행지)

@Component
public class TripFilterStrategyProvider {
    private final List<TripFilterStrategy> tripFilterStrategies;

    public TripFilterStrategyProvider(final List<TripFilterStrategy> tripFilterStrategies) {
        this.tripFilterStrategies = tripFilterStrategies;
    }

    public TripFilterStrategy findTripsByFilterStrategy(final String filterStrategyName) {
        return tripFilterStrategies.stream()
                .filter(tripFilterStrategy -> tripFilterStrategy.isMatch(filterStrategyName))
                .findFirst()
                .orElseThrow();
    }
}
