package moheng.recommendtrip.domain.tripfilterstrategy;

import org.springframework.stereotype.Component;
import java.util.List;

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
