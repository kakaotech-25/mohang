package moheng.recommendtrip.domain;

import moheng.trip.domain.Trip;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TripLiveInformationFilterStrategy implements TripFilterStrategy {
    private static final String STRATEGY_NAME = "LIVE_INFO";

    @Override
    public boolean isMatch(final String strategyName) {
        return STRATEGY_NAME.equals(strategyName);
    }

    @Override
    public List<Trip> execute() {
        return List.of();
    }
}
