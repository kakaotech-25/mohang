package moheng.trip.domain.recommendstrategy;

import moheng.member.domain.Member;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.trip.domain.Trip;

import java.util.List;

public interface RecommendTripStrategy {
    void execute(final Trip trip, final Member member, final List<RecommendTrip> recommendTrips);
    boolean isMatch(final long recommendSize, final long maxSize, final long minSize);
}
