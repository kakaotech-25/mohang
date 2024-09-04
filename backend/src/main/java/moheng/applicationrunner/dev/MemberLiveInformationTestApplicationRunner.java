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

@Order(1)
@Component
public class MemberLiveInformationTestApplicationRunner implements ApplicationRunner {
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final MemberRepository memberRepository;
    private final LiveInformationRepository liveInformationRepository;

    public MemberLiveInformationTestApplicationRunner(final MemberLiveInformationRepository memberLiveInformationRepository,
                                                      final MemberRepository memberRepository,
                                                      final LiveInformationRepository liveInformationRepository) {
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.memberRepository = memberRepository;
        this.liveInformationRepository = liveInformationRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("qwekqwekopqwekpoqwkpodmvnndfbiherwbnevwiuui");
    }
}