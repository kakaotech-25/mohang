package moheng.liveinformation.domain;

import static moheng.fixture.LiveInformationFixture.*;
import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import moheng.config.slice.RepositoryTestConfig;
import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.repository.MemberLiveInformationRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
        Member 하온 = memberRepository.save(하온_기존());

        LiveInformation 생활정보1 = liveInformationRepository.save(new LiveInformation("생활정보1"));
        LiveInformation 생활정보2 = liveInformationRepository.save(new LiveInformation("생활정보2"));

        MemberLiveInformation memberLiveInformation1 = new MemberLiveInformation(생활정보1, 하온);
        MemberLiveInformation memberLiveInformation2 = new MemberLiveInformation(생활정보2, 하온);
        List<MemberLiveInformation> memberLiveInformations = new ArrayList<>(List.of(memberLiveInformation1, memberLiveInformation2));
        memberLiveInformationRepository.saveAll(memberLiveInformations);

        // when, then
        assertThat(memberLiveInformationRepository.findByMemberId(하온.getId())).hasSize(2);
    }

    @DisplayName("멤버의 생활정보를 삭제한다.")
    @Test
    void 멤버의_생활정보를_삭제한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());

        LiveInformation 생활정보1 = liveInformationRepository.save(생활정보1_생성());
        LiveInformation 생활정보2 = liveInformationRepository.save(생활정보2_생성());

        MemberLiveInformation 멤버_생활정보1 = new MemberLiveInformation(생활정보1, 하온);
        MemberLiveInformation 멤버_생활정보2 = new MemberLiveInformation(생활정보2, 하온);
        memberLiveInformationRepository.saveAll(List.of(멤버_생활정보1, 멤버_생활정보2));

        // when
        memberLiveInformationRepository.deleteByMemberId(하온.getId());

        // then
        assertThat(memberLiveInformationRepository.count()).isEqualTo(0);
    }
}
