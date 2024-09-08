package moheng.applicationrunner.dev;

import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.repository.MemberLiveInformationRepository;
import moheng.member.domain.GenderType;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.trip.domain.repository.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Order(4)
@Component
public class MemberLiveInformationDevApplicationRunner implements ApplicationRunner {
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final MemberRepository memberRepository;
    private final RecommendTripRepository recommendTripRepository;
    private final TripRepository tripRepository;

    public MemberLiveInformationDevApplicationRunner(final MemberLiveInformationRepository memberLiveInformationRepository,
                                                     final MemberRepository memberRepository,
                                                     final RecommendTripRepository recommendTripRepository,
                                                     final TripRepository tripRepository) {
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.memberRepository = memberRepository;
        this.recommendTripRepository = recommendTripRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Member member = memberRepository.save(new Member(1L, "msung6924@kakao.com", "devhaon", "https://default.png", SocialType.KAKAO,
                LocalDate.of(2000, 1, 1), GenderType.MEN));

        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(1L).get(), member, 1L));
        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(2L).get(), member, 2L));
        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(3L).get(), member, 3L));
        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(4L).get(), member, 4L));
        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(5L).get(), member, 5L));
        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(6L).get(), member, 6L));
        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(7L).get(), member, 7L));
        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(8L).get(), member, 8L));
        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(9L).get(), member, 9L));
        recommendTripRepository.save(new RecommendTrip(tripRepository.findById(10L).get(), member, 10L));
    }
}
