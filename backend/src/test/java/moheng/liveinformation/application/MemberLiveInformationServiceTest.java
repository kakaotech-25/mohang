package moheng.liveinformation.application;

import static moheng.fixture.MemberFixtures.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.config.ServiceTestConfig;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.MemberLiveInformation;
import moheng.liveinformation.domain.MemberLiveInformationService;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MemberLiveInformationServiceTest extends ServiceTestConfig {
    @Autowired
    private MemberLiveInformationService memberLiveInformationService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private LiveInformationService liveInformationService;

    @DisplayName("멤버의 생활정보 리스트를 저장한다.")
    @Test
    void 멤버의_생활정보_리스트를_저장한다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);

        LiveInformation liveInformation1 = liveInformationService.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationService.save(new LiveInformation("생활정보2"));

        MemberLiveInformation memberLiveInformation1 = new MemberLiveInformation(liveInformation1, member);
        MemberLiveInformation memberLiveInformation2 = new MemberLiveInformation(liveInformation2, member);
        List<MemberLiveInformation> memberLiveInformations = new ArrayList<>(List.of(memberLiveInformation1, memberLiveInformation2));

        // when, then
        assertDoesNotThrow(() -> memberLiveInformationService.saveAll(memberLiveInformations));
    }
}
