package moheng.liveinformation.application;

import static moheng.fixture.LiveInformationFixture.*;
import static moheng.fixture.LiveInformationFixture.생활정보1_생성;
import static moheng.fixture.MemberLiveInformationFixture.*;
import static moheng.fixture.MemberFixtures.*;
import static moheng.fixture.MemberLiveInformationFixture.멤버_생활정보_업데이트_요청;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import moheng.member.domain.repository.MemberRepository;
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
    private MemberRepository memberRepository;

    @Autowired
    private LiveInformationService liveInformationService;

    @DisplayName("멤버의 생활정보 리스트를 저장한다.")
    @Test
    void 멤버의_생활정보_리스트를_저장한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());

        LiveInformation 생활정보1 = liveInformationService.save(생활정보1_생성());
        LiveInformation 생활정보2 = liveInformationService.save(생활정보2_생성());

        MemberLiveInformation 멤버_생활정보1 = new MemberLiveInformation(생활정보1, 하온);
        MemberLiveInformation 멤버_생활정보2 = new MemberLiveInformation(생활정보2, 하온);
        List<MemberLiveInformation> 멤버_생활정보_리스트 = new ArrayList<>(List.of(멤버_생활정보1, 멤버_생활정보2));

        // when, then
        assertDoesNotThrow(() -> memberLiveInformationService.saveAll(멤버_생활정보_리스트));
    }

    @DisplayName("멤버가 선택한 생활정보와 선택하지 않은 생활정보를 모두 조회한다.")
    @Test
    void 멤버가_선택한_생활정보와_선택하지_않은_생화정보를_모두_조회한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());

        LiveInformation 생활정보1 = liveInformationService.save(생활정보1_생성());
        LiveInformation 생활정보2 = liveInformationService.save(생활정보2_생성());
        LiveInformation 선택_안한_생활정보3 = liveInformationService.save(생활정보3_생성());

        MemberLiveInformation 멤버_생활정보1 = new MemberLiveInformation(생활정보1, 하온);
        MemberLiveInformation 멤버_생활정보2 = new MemberLiveInformation(생활정보2, 하온);
        List<MemberLiveInformation> 멤버_생활정보_리스트 = new ArrayList<>(List.of(멤버_생활정보1, 멤버_생활정보2));

        memberLiveInformationService.saveAll(멤버_생활정보_리스트);

        // when
        List<LiveInfoResponse> expected = memberLiveInformationService.findMemberSelectedLiveInformation(하온.getId()).getLiveInfoResponses();

        // then
        assertThat(expected).hasSize(3);
    }

    @DisplayName("멤버의 생활정보를 수정한다.")
    @Test
    void 멤버의_생활정보를_수정한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());

        LiveInformation 생활정보1 = liveInformationService.save(생활정보1_생성());
        LiveInformation 생활정보2 = liveInformationService.save(생활정보2_생성());

        // when, then
        assertDoesNotThrow(() -> memberLiveInformationService.updateMemberLiveInformation(하온.getId(), 멤버_생활정보_업데이트_요청(List.of(생활정보1.getId(), 생활정보2.getId()))));
    }

    @DisplayName("존재하지 않는 멤버의 생활정보 리스트를 수정하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버의_생활정보_리스트를_수정하면_예외가_발생한다() {
        // given
        long 유효하지_않는_멤버_ID = -1L;
        LiveInformation 생활정보1 = liveInformationService.save(생활정보1_생성());
        LiveInformation 생활정보2 = liveInformationService.save(생활정보2_생성());

        // when, then
        assertThatThrownBy(() -> memberLiveInformationService.updateMemberLiveInformation(유효하지_않는_멤버_ID, 멤버_생활정보_업데이트_요청(List.of(생활정보1.getId(), 생활정보2.getId()))))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("생활정보를 수정시 멤버의 기존 생활정보를 모두 삭제하고 새로운 생활정보를 생성한다.")
    @Test
    void 생활정보를_수정시_멤버의_기존_생활정보를_모두_삭제하고_새로운_생활정보를_생성한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());

        LiveInformation 생활정보1 = liveInformationService.save(new LiveInformation("생활정보1"));
        LiveInformation 생활정보2 = liveInformationService.save(new LiveInformation("생활정보2"));
        LiveInformation 생활정보3 = liveInformationService.save(new LiveInformation("생활정보3"));
        memberLiveInformationService.saveAll(List.of(new MemberLiveInformation(생활정보1, 하온), new MemberLiveInformation(생활정보2, 하온)));

        // when
        memberLiveInformationService.updateMemberLiveInformation(하온.getId(), 멤버_생활정보_업데이트_요청(List.of(생활정보1.getId(), 생활정보2.getId(), 생활정보3.getId())));

        // then
         assertThat(memberLiveInformationService.findMemberLiveInfoIds(하온.getId())).hasSize(3);
    }

    @DisplayName("업데이트 하려는 멤버 생활정보중에 일부 생활정보가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void 업데이트_하려는_멤버_생활정보중에_일부_생활정보가_존재하지_않을_경우_예외가_발생한다() {
        // given
        List<Long> 유효하지_않은_멤버_생활정보_ID_리스트 = List.of(-1L, -2L, -3L);
        Member 하온 = memberRepository.save(하온_기존());

        // when, then
        assertThatThrownBy(() -> memberLiveInformationService.updateMemberLiveInformation(하온.getId(), 멤버_생활정보_업데이트_요청(유효하지_않은_멤버_생활정보_ID_리스트)))
                .isInstanceOf(NoExistLiveInformationException.class);
    }
}
