package moheng.applicationrunner.warmup;

import moheng.member.application.MemberService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile({"dev", "prod"})
@Order(5)
@Component
public class MemberWarmUpRunner implements ApplicationRunner {
    private final MemberService memberService;

    public MemberWarmUpRunner(final MemberService memberService) {
        this.memberService = memberService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            memberService.findByEmail("email");
            memberService.findMemberAuthorityAndProfileImg(1L);
        } catch (Exception e) {

        }
    }
}
