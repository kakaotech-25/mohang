package moheng.applicationrunner.dev;

import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.repository.MemberLiveInformationRepository;
import moheng.member.domain.repository.MemberRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class MemberLiveInformationDevApplicationRunner implements ApplicationRunner {
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final MemberRepository memberRepository;
    private final LiveInformationRepository liveInformationRepository;

    public MemberLiveInformationDevApplicationRunner(final MemberLiveInformationRepository memberLiveInformationRepository,
                                                     final MemberRepository memberRepository,
                                                     final LiveInformationRepository liveInformationRepository) {
        this.memberLiveInformationRepository = memberLiveInformationRepository;
        this.memberRepository = memberRepository;
        this.liveInformationRepository = liveInformationRepository;
    }

    @Override
    public void run(ApplicationArguments args) {

    }
}
