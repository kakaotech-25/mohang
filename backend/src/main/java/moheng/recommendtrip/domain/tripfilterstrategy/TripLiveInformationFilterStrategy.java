package moheng.recommendtrip.domain.tripfilterstrategy;

import moheng.recommendtrip.domain.filterinfo.FilterStandardInfo;
import moheng.trip.domain.Trip;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TripLiveInformationFilterStrategy implements TripFilterStrategy {
    private static final String STRATEGY_NAME = "LIVE_INFO";

    public TripLiveInformationFilterStrategy() {
    }

    @Override
    public boolean isMatch(final String strategyName) {
        return STRATEGY_NAME.equals(strategyName);
    }

    @Override
    public List<Trip> execute(final FilterStandardInfo filterInfo) {
        return List.of();
    }
}
