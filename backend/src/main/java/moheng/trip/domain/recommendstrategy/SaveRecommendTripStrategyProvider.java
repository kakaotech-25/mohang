package moheng.trip.domain.recommendstrategy;

import moheng.trip.exception.NoExistRecommendTripStrategyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaveRecommendTripStrategyProvider {
    private static final long MAX_SAVE_RECOMMEND_TRIPS_SIZE = 10L;
    private static final long MIN_SAVE_RECOMMEND_TRIPS_SIZE = 5L;
    private final List<RecommendTripStrategy> recommendTripStrategies;

    public SaveRecommendTripStrategyProvider(final List<RecommendTripStrategy> recommendTripStrategies) {
        this.recommendTripStrategies = recommendTripStrategies;
    }

    public RecommendTripStrategy findRecommendTripStrategy(final long recommendSize) {
        return recommendTripStrategies.stream()
                .filter(recommendTripStrategy -> recommendTripStrategy.isMatch(recommendSize, MAX_SAVE_RECOMMEND_TRIPS_SIZE, MIN_SAVE_RECOMMEND_TRIPS_SIZE))
                .findFirst()
                .orElseThrow(NoExistRecommendTripStrategyException::new);
    }
}
