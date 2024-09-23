package moheng.applicationrunner.dev;

import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.repository.MemberLiveInformationRepository;
import moheng.member.domain.GenderType;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.trip.domain.MemberTrip;
import moheng.trip.domain.repository.MemberTripRepository;
import moheng.trip.domain.repository.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Profile("dev")
@Order(4)
@Component
public class MemberLiveInformationDevApplicationRunner implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final RecommendTripRepository recommendTripRepository;
    private final TripRepository tripRepository;
    private final MemberTripRepository memberTripRepository;

    public MemberLiveInformationDevApplicationRunner(final MemberRepository memberRepository,
                                                     final RecommendTripRepository recommendTripRepository,
                                                     final TripRepository tripRepository,
                                                     final MemberTripRepository memberTripRepository) {
        this.memberRepository = memberRepository;
        this.recommendTripRepository = recommendTripRepository;
        this.tripRepository = tripRepository;
        this.memberTripRepository = memberTripRepository;
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

        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(1L).get(), 1L));
        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(2L).get(), 1L));
        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(3L).get(), 2L));
        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(4L).get(), 3L));
        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(5L).get(), 4L));
        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(6L).get(), 5L));
        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(7L).get(), 6L));
        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(8L).get(), 7L));
        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(9L).get(), 8L));
        memberTripRepository.save(new MemberTrip(member, tripRepository.findById(10L).get(), 9L));
    }
}
