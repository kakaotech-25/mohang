package moheng.applicationrunner.dev;


import moheng.member.domain.GenderType;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import moheng.member.domain.repository.MemberRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Order(1)
@Component
public class MemberApplicationRunner implements ApplicationRunner {
    private final MemberRepository memberRepository;

    public MemberApplicationRunner(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        memberRepository.save(new Member(1L, "msung6924@kakao.com", "moheng",
                "https://default.png", SocialType.KAKAO,
                LocalDate.of(2000, 1, 1), GenderType.MEN));
    }
}
