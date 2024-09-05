package moheng.applicationrunner.prod;

import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.repository.MemberLiveInformationRepository;
import moheng.member.domain.repository.MemberRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;


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

    }
}
