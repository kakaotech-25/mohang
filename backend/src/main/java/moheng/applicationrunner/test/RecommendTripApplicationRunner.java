package moheng.applicationrunner.test;

import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(8)
@Component
public class RecommendTripApplicationRunner implements ApplicationRunner {
    private final RecommendTripRepository recommendTripRepository;
    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;

    public RecommendTripApplicationRunner(final RecommendTripRepository recommendTripRepository,
                                          final MemberRepository memberRepository,
                                          final TripRepository tripRepository) {
        this.recommendTripRepository = recommendTripRepository;
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Member member = memberRepository.findById(1L).get();
        Trip trip1 = tripRepository.findById(1L).get();
        Trip trip2 = tripRepository.findById(2L).get();
        Trip trip3 = tripRepository.findById(3L).get();
        Trip trip4 = tripRepository.findById(4L).get();
        Trip trip5 = tripRepository.findById(5L).get();
        Trip trip6 = tripRepository.findById(6L).get();
        Trip trip7 = tripRepository.findById(7L).get();
        Trip trip8 = tripRepository.findById(8L).get();
        Trip trip9 = tripRepository.findById(9L).get();

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L));

    }
}
