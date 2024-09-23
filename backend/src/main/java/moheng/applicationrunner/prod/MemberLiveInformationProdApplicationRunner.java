package moheng.applicationrunner.prod;

import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.repository.MemberLiveInformationRepository;
import moheng.member.domain.repository.MemberRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile("prod")
@Order(4)
@Component
public class MemberLiveInformationProdApplicationRunner implements ApplicationRunner {
    private final MemberLiveInformationRepository memberLiveInformationRepository;
    private final MemberRepository memberRepository;
    private final LiveInformationRepository liveInformationRepository;

    public MemberLiveInformationProdApplicationRunner(final MemberLiveInformationRepository memberLiveInformationRepository,
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
