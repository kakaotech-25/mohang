package moheng.liveinformation.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThat;

import moheng.config.RepositoryTestConfig;
import moheng.config.TestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = TestConfig.class)
public class MemberLiveInformationRepositoryTest extends RepositoryTestConfig {
    @Autowired
    private MemberLiveInformationRepository memberLiveInformationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LiveInformationRepository liveInformationRepository;

    @DisplayName("멤버의 생활정보를 찾는다.")
    @Test
    void 멤버의_생활정보를_찾는다() {
        // given
        memberRepository.save(하온_기존());
        Member member = memberRepository.findByEmail(하온_이메일).get();

        LiveInformation liveInformation1 = liveInformationRepository.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationRepository.save(new LiveInformation("생활정보2"));

        MemberLiveInformation memberLiveInformation1 = new MemberLiveInformation(liveInformation1, member);
        MemberLiveInformation memberLiveInformation2 = new MemberLiveInformation(liveInformation2, member);
        List<MemberLiveInformation> memberLiveInformations = new ArrayList<>(List.of(memberLiveInformation1, memberLiveInformation2));
        memberLiveInformationRepository.saveAll(memberLiveInformations);

        // when, then
        assertThat(memberLiveInformationRepository.findByMemberId(member.getId())).hasSize(2);
    }

    @DisplayName("멤버의 생활정보를 삭제한다.")
    @Test
    void 멤버의_생활정보를_삭제한다() {
        // given
        memberRepository.save(하온_기존());
        Member member = memberRepository.findByEmail(하온_이메일).get();

        LiveInformation liveInformation1 = liveInformationRepository.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationRepository.save(new LiveInformation("생활정보2"));

        MemberLiveInformation memberLiveInformation1 = new MemberLiveInformation(liveInformation1, member);
        MemberLiveInformation memberLiveInformation2 = new MemberLiveInformation(liveInformation2, member);
        List<MemberLiveInformation> memberLiveInformations = new ArrayList<>(List.of(memberLiveInformation1, memberLiveInformation2));
        memberLiveInformationRepository.saveAll(memberLiveInformations);

        // when
        memberLiveInformationRepository.deleteByMemberId(member.getId());

        // then
        assertThat(memberLiveInformationRepository.count()).isEqualTo(0);
    }
}
