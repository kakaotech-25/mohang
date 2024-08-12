package moheng.recommendtrip.application;

import moheng.member.domain.Member;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class RecommendTripService {
    private final RecommendTripRepository recommendTripRepository;

    public RecommendTripService(final RecommendTripRepository recommendTripRepository) {
        this.recommendTripRepository = recommendTripRepository;
    }

    @Transactional
    public void saveByRank(Trip trip, Member member, long rank) {
        recommendTripRepository.save(new RecommendTrip(trip, member, rank));
    }
}
