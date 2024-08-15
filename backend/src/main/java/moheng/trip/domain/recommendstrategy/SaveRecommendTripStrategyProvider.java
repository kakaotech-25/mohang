package moheng.trip.domain.recommendstrategy;

import moheng.trip.exception.NoExistRecommendTripStrategyException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaveRecommendTripStrategyProvider {
    private final List<RecommendTripStrategy> recommendTripStrategies;

    public SaveRecommendTripStrategyProvider(final List<RecommendTripStrategy> recommendTripStrategies) {
        this.recommendTripStrategies = recommendTripStrategies;
    }

    public RecommendTripStrategy findRecommendTripStrategy(final long recommendSize) {
        return recommendTripStrategies.stream()
                .filter(recommendTripStrategy -> recommendTripStrategy.isMatch(recommendSize))
                .findFirst()
                .orElseThrow(NoExistRecommendTripStrategyException::new);
    }
}
