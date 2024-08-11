package moheng.recommendtrip.application;

import moheng.member.domain.Member;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import org.springframework.stereotype.Service;

@Service
public class RecommendTripService {
    private final RecommendTripRepository recommendTripRepository;

    public RecommendTripService(final RecommendTripRepository recommendTripRepository) {
        this.recommendTripRepository = recommendTripRepository;
    }

    public void saveByRank(Trip trip, Member member, long rank) {
        recommendTripRepository.save(new RecommendTrip(trip, member, rank));
    }
}
