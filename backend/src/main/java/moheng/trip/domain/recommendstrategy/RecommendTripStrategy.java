package moheng.trip.domain.recommendstrategy;

import moheng.member.domain.Member;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.trip.domain.Trip;

import java.util.List;

public interface RecommendTripStrategy {
    void execute(Trip trip, Member member, List<RecommendTrip> recommendTrips);
}
