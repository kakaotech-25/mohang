package moheng.applicationrunner.dev;

import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.domain.MemberLiveInformation;
import moheng.liveinformation.domain.MemberLiveInformationRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(5)
@Component
public class MemberLiveInformationApplicationRunner implements ApplicationRunner {
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final MemberRepository memberRepository;
    private final LiveInformationRepository liveInformationRepository;

    public MemberLiveInformationApplicationRunner(final MemberLiveInformationRepository memberLiveInformationRepository,
                                                  final MemberRepository memberRepository,
                                                  final LiveInformationRepository liveInformationRepository) {
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.memberRepository = memberRepository;
        this.liveInformationRepository = liveInformationRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Member member = memberRepository.findById(1L).get();
        LiveInformation liveInformation1 = liveInformationRepository.findById(1L).get();
        LiveInformation liveInformation2 = liveInformationRepository.findById(2L).get();
        LiveInformation liveInformation3 = liveInformationRepository.findById(3L).get();

        memberLiveInformationRepository.save(new MemberLiveInformation(liveInformation1, member));
        memberLiveInformationRepository.save(new MemberLiveInformation(liveInformation2, member));
        memberLiveInformationRepository.save(new MemberLiveInformation(liveInformation3, member));
    }
}
