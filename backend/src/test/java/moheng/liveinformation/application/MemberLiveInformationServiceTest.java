package moheng.liveinformation.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThat;

import moheng.config.slice.ServiceTestConfig;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.MemberLiveInformation;
import moheng.liveinformation.dto.LiveInfoResponse;
import moheng.liveinformation.dto.UpdateMemberLiveInformationRequest;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import moheng.member.exception.NoExistMemberException;
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

    @DisplayName("멤버가 선택한 생활정보와 선택하지 않은 생활정보를 모두 조회한다.")
    @Test
    void 멤버가_선택한_생활정보와_선택하지_않은_생화정보를_모두_조회한다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);

        LiveInformation liveInformation1 = liveInformationService.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationService.save(new LiveInformation("생활정보2"));
        LiveInformation liveInformation3 = liveInformationService.save(new LiveInformation("생활정보3"));

        MemberLiveInformation memberLiveInformation1 = new MemberLiveInformation(liveInformation1, member);
        MemberLiveInformation memberLiveInformation2 = new MemberLiveInformation(liveInformation2, member);
        List<MemberLiveInformation> memberLiveInformations = new ArrayList<>(List.of(memberLiveInformation1, memberLiveInformation2));

        memberLiveInformationService.saveAll(memberLiveInformations);

        // when
        List<LiveInfoResponse> expected = memberLiveInformationService.findMemberSelectedLiveInformation(member.getId()).getLiveInfoResponses();

        // then
        assertThat(expected).hasSize(3);
    }

    @DisplayName("멤버의 생활정보를 수정한다.")
    @Test
    void 멤버의_생활정보를_수정한다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);

        LiveInformation liveInformation1 = liveInformationService.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationService.save(new LiveInformation("생활정보2"));

        // when, then
        UpdateMemberLiveInformationRequest request = new UpdateMemberLiveInformationRequest(List.of(1L, 2L));
        assertDoesNotThrow(() -> memberLiveInformationService.updateMemberLiveInformation(member.getId(), request));
    }

    @DisplayName("존재하지 않는 멤버의 생활정보 리스트를 수정하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버의_생활정보_리스트를_수정하면_예외가_발생한다() {
        // given
        LiveInformation liveInformation1 = liveInformationService.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationService.save(new LiveInformation("생활정보2"));

        // when, then
        UpdateMemberLiveInformationRequest request = new UpdateMemberLiveInformationRequest(List.of(1L, 2L));
        assertThatThrownBy(() -> memberLiveInformationService.updateMemberLiveInformation(-1L, request))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("생활정보를 수정시 멤버의 기존 생활정보를 모두 삭제하고 새로운 생활정보를 생성한다.")
    @Test
    void 생활정보를_수정시_멤버의_기존_생활정보를_모두_삭제하고_새로운_생활정보를_생성한다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);

        LiveInformation liveInformation1 = liveInformationService.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationService.save(new LiveInformation("생활정보2"));
        LiveInformation liveInformation3 = liveInformationService.save(new LiveInformation("생활정보3"));

        MemberLiveInformation oldMemberLiveInformation1 = new MemberLiveInformation(liveInformation1, member);
        MemberLiveInformation oldMemberLiveInformation2 = new MemberLiveInformation(liveInformation2, member);
        List<MemberLiveInformation> oldMemberLiveInformations = new ArrayList<>(List.of(oldMemberLiveInformation1, oldMemberLiveInformation2));
        memberLiveInformationService.saveAll(oldMemberLiveInformations);

        // when
        UpdateMemberLiveInformationRequest request = new UpdateMemberLiveInformationRequest(List.of(1L, 2L, 3L));
        memberLiveInformationService.updateMemberLiveInformation(member.getId(), request);

        // then
         assertThat(memberLiveInformationService.findMemberLiveInfoIds(member.getId())).hasSize(3);
    }

    @DisplayName("업데이트 하려는 멤버 생활정보중에 일부 생활정보가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void 업데이트_하려는_멤버_생활정보중에_일부_생활정보가_존재하지_않을_경우_예외가_발생한다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);

        // when, then
        UpdateMemberLiveInformationRequest request = new UpdateMemberLiveInformationRequest(List.of(-1L, -2L, -3L));
        assertThatThrownBy(() -> memberLiveInformationService.updateMemberLiveInformation(member.getId(), request))
                .isInstanceOf(NoExistLiveInformationException.class);
    }
}
