package moheng.recommendtrip.domain.tripfilterstrategy;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.MemberLiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import moheng.recommendtrip.domain.PreferredLocationsProvider;
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
    public List<Trip> execute(final long memberId) {
        return List.of();
    }
}
