package moheng.recommendtrip.domain.tripfilterstrategy;

import moheng.recommendtrip.domain.filterinfo.FilterStandardInfo;
import moheng.trip.domain.Trip;

import java.util.List;

public interface TripFilterStrategy {
    boolean isMatch(final String strategyName);
    List<Trip> execute(final FilterStandardInfo filterStandardInfo);
}
