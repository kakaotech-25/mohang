package moheng.member.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.auth.domain.oauth.Authority;
import moheng.auth.exception.NoExistMemberTokenException;
import moheng.config.ServiceTestConfig;
import moheng.liveinformation.application.LiveInformationService;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.exception.EmptyLiveInformationException;
import moheng.member.domain.GenderType;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.dto.request.SignUpInterestTripsRequest;
import moheng.member.dto.request.SignUpLiveInfoRequest;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.member.dto.request.UpdateProfileRequest;
import moheng.member.dto.response.CheckDuplicateNicknameResponse;
import moheng.member.exception.DuplicateNicknameException;
import moheng.member.exception.NoExistMemberException;
import moheng.member.exception.ShortContentidsSizeException;
import moheng.trip.application.TripService;
import moheng.trip.domain.Trip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MemberServiceTest extends ServiceTestConfig {
    @Autowired
    private MemberService memberService;

    @Autowired
    private LiveInformationService liveInformationService;

    @Autowired
    private TripService tripService;

    @DisplayName("회원을 저장한다.")
    @Test
    void 회원을_저장한다() {
        // given
        Member member = new Member(하온_이메일, 하온_소셜_타입_카카오);

        // when, then
        assertDoesNotThrow(() -> memberService.save(member));
    }

    @DisplayName("소셜 로그인을 시도한 회원을 저장한다.")
    @Test
    void 소셜_로그인을_시도한_회원을_저장한다() {
        // given
        Member member = new Member(하온_이메일, 하온_소셜_타입_카카오);

        // when, then
        assertDoesNotThrow(() -> memberService.save(member));
    }

    @DisplayName("이메일로 회원을 찾는다.")
    @Test
    void 이메일로_회원을_찾는다() {
        // given
        String email = "msung6924@naver.com";
        String nickname = "msung99";
        String profileImageUrl = "https://image";

        Member member = new Member(리안_이메일, SocialType.KAKAO);
        memberService.save(member);

        // when
        Member foundMember = memberService.findByEmail(리안_이메일);

        // then
        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @DisplayName("주어진 이메일로 가입된 회원이 존재하는지 확인한다.")
    @Test
    void 주어진_이메일로_가입된_회원이_존재하는지_확인한다() {
        // given

        Member member = new Member(래오_이메일, 래오_소셜_타입_카카오);
        memberService.save(member);

        // when
        boolean actual = memberService.existsByEmail(래오_이메일);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이미 중복되는 닉네임이 존재한다면 참을 리턴한다.")
    @Test
    void 이미_존재하는_닉네임이_존재한다면_참을_리턴한다() {
        // given
        memberService.save(하온_기존());

        // when, then
        boolean actual = memberService.existsByNickname(하온_닉네임);

       assertThat(actual).isTrue();
    }

    @DisplayName("프로필 정보를 입력하여 회원가입한다.")
    @Test
    void 프로필_정보를_입력하여_회원가입한다() {
        // given
        Member member = new Member(하온_이메일, 하온_소셜_타입_카카오);
        memberService.save(member);
        SignUpProfileRequest signUpProfileRequest = new SignUpProfileRequest(하온_닉네임, 하온_생년월일, 하온_성별, 하온_프로필_경로);

        assertDoesNotThrow(() ->
                memberService.signUpByProfile(member.getId(), signUpProfileRequest));
    }

    @DisplayName("존재하지 않는 회원의 프로필 정보로 회원가입하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_회원의_프로필_정보로_회원가입하면_예외가_발생한다() {
        // given
        SignUpProfileRequest signUpProfileRequest = new SignUpProfileRequest(하온_닉네임, 하온_생년월일, 하온_성별, 하온_프로필_경로);

        // when, then
        assertThatThrownBy(() -> memberService.signUpByProfile(-1L, signUpProfileRequest))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("중복되는 닉네임이 존재하면 예외가 발생한다.")
    @Test
    void 중복되는_닉네임이_존재하면_예외가_발생한다() {
        // given
        memberService.save(하온_기존());
        memberService.save(래오_기존());

        SignUpProfileRequest signUpProfileRequest = new SignUpProfileRequest(래오_닉네임, 하온_생년월일, 하온_성별, 하온_프로필_경로);

        // when, then
        assertThatThrownBy(() ->
                memberService.signUpByProfile(1L, signUpProfileRequest))
                .isInstanceOf(DuplicateNicknameException.class);
    }

    @DisplayName("회원의 프로필을 업데이트한다.")
    @Test
    void 회원의_프로필을_업데이트한다() {
        // given
        memberService.save(하온_기존());
        UpdateProfileRequest request = new UpdateProfileRequest(하온_닉네임, 하온_생년월일, 하온_성별, 하온_프로필_경로);
        long memberId = memberService.findByEmail(하온_이메일).getId();

        // when, then
        assertDoesNotThrow(() -> memberService.updateByProfile(memberId, request));
    }

    @DisplayName("존재하지 않는 회원의 프로필을 업데이트하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_회원의_프로필을_업데이트하면_예외가_발생한다() {
        // given
        UpdateProfileRequest request = new UpdateProfileRequest(하온_닉네임, 하온_생년월일, 하온_성별, 하온_프로필_경로);

        // when, then
        assertThatThrownBy(() -> memberService.updateByProfile(1L, request))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("기존 닉네임과 다르다면 해당 닉네임을 가진 다른 유저의 닉네임과 중복 여부를 확인한다.")
    @Test
    void 기존_닉네임과_다르다면_해당_닉네임을_가진_다른_유저의_닉네임과_중복_여부를_확인한다() {
        // given
        memberService.save(하온_기존());
        memberService.save(래오_기존());
        Member member = memberService.findByEmail(하온_이메일);

        UpdateProfileRequest request = new UpdateProfileRequest(래오_닉네임, 하온_생년월일, 하온_성별, 하온_프로필_경로);

        // when, then
        assertThatThrownBy(() -> memberService.updateByProfile(member.getId(), request))
                .isInstanceOf(DuplicateNicknameException.class);
    }

    @DisplayName("회원 본인을 제외한 다른 회원들중에 닉네임이 중복된다면 예외가 발생한다.")
    @Test
    void 회원_본인을_제외한_다른_회원들중에_닉네임이_중복된다면_예외가_발생한다() {
        // given
        memberService.save(하온_기존());
        memberService.save(래오_기존());
        UpdateProfileRequest request = new UpdateProfileRequest(래오_닉네임, 하온_생년월일, 하온_성별, 하온_프로필_경로);
        long memberId = memberService.findByEmail(하온_이메일).getId();

        // when, then
        assertThatThrownBy(() -> memberService.updateByProfile(memberId, request))
                .isInstanceOf(DuplicateNicknameException.class);
    }

    @DisplayName("회원의 생활정보를 저장한다.")
    @Test
    void 회원의_생활정보를_저장한다() {
        // given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();

        liveInformationService.save(new LiveInformation("생활정보1"));
        liveInformationService.save(new LiveInformation("생활정보2"));
        SignUpLiveInfoRequest request = new SignUpLiveInfoRequest(List.of("생활정보1", "생활정보2"));

        // when, then
        assertDoesNotThrow(() ->memberService.signUpByLiveInfo(memberId, request));
    }

    @DisplayName("존재하지 않는 회원의 생활정보를 추가하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_회원의_생활정보를_추가하면_예외가_발생한다() {
        // given
        SignUpLiveInfoRequest request = new SignUpLiveInfoRequest(List.of("생활정보1", "생활정보2"));

        // when, then
        assertThatThrownBy(() -> memberService.signUpByLiveInfo(-1L, request))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("생활정보를 선택하지 않고 비어있다면 예외가 발생한다.")
    @Test
    void 생활정보를_선택하지_않고_비어있다면_예외가_발생한다() {
        // given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();
        SignUpLiveInfoRequest request = new SignUpLiveInfoRequest(new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> memberService.signUpByLiveInfo(memberId, request))
                .isInstanceOf(EmptyLiveInformationException.class);
    }

    @DisplayName("생활정보를 선택하지 않고 null 을 전달하면 예외가 발생한다.")
    @Test
    void 생활정보를_선택하지_않고_null을_전달하면_예외가_발생한다() {
        // given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();
        SignUpLiveInfoRequest request = new SignUpLiveInfoRequest(null);

        // when, then
        assertThatThrownBy(() -> memberService.signUpByLiveInfo(memberId, request))
                .isInstanceOf(EmptyLiveInformationException.class);
    }

    @DisplayName("회원의 관심 여행지를 저장한다.")
    @Test
    void 회원의_관심_여행지를_저장한다() {
        // given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();

        for(long contentId=1; contentId<=5; contentId++) {
            tripService.save(new Trip("롯데월드", "서울특별시 송파구", contentId,
                    "설명", "https://image.com"));
        }
        SignUpInterestTripsRequest request = new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L));

        // when, then
        assertDoesNotThrow(() -> memberService.signUpByInterestTrips(memberId, request));
    }

    @DisplayName("존재하지 않는 회원의 관심 여행지를 저장하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_회원의_관심_여행지를_저장하면_예외가_발생한다() {
        // given
        for(long contentId=1; contentId<=5; contentId++) {
            tripService.save(new Trip("롯데월드", "서울특별시 송파구", contentId,
                    "설명", "https://image.com"));
        }
        SignUpInterestTripsRequest request = new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L));

        // when, then
        assertThatThrownBy(() -> memberService.signUpByInterestTrips(-1L, request))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("회원의 관심 여행지가 5개 미만이라면 예외가 발생한다.")
    @Test
    void 회원의_관심_여행지가_5개_미만이라면_예외가_발생한다() {
        // given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();

        for(long contentId=1; contentId<=3; contentId++) {
            tripService.save(new Trip("롯데월드", "서울특별시 송파구", contentId,
                    "설명", "https://image.com"));
        }
        SignUpInterestTripsRequest request = new SignUpInterestTripsRequest(List.of(1L, 2L, 3L));

        // when, then
        assertThatThrownBy(() -> memberService.signUpByInterestTrips(memberId, request))
                .isInstanceOf(ShortContentidsSizeException.class);
    }

    @DisplayName("회원의 관심 여행지가 10개를 초과하면 예외가 발생한다.")
    @Test
    void 회원의_관심_여행지가_10개를_초과하면_예외가_발생한다() {
        // given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();

        for(long contentId=1; contentId<=12; contentId++) {
            tripService.save(new Trip("롯데월드", "서울특별시 송파구", contentId,
                    "설명", "https://image.com"));
        }
        SignUpInterestTripsRequest request = new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L));

        // when, then
        assertThatThrownBy(() -> memberService.signUpByInterestTrips(memberId, request))
                .isInstanceOf(ShortContentidsSizeException.class);
    }

    @DisplayName("관심 여행지를 입력을 마치면 유저의 권한을 정규 회원으로 승격한다.")
    @Test
    void 관심_여행지_입력을_마치면_유저의_권한을_정규_회원으로_승격한다() {
        // given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();

        for(long contentId=1; contentId<=5; contentId++) {
            tripService.save(new Trip("롯데월드", "서울특별시 송파구", contentId,
                    "설명", "https://image.com"));
        }
        SignUpInterestTripsRequest request = new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L));

        // when
        memberService.signUpByInterestTrips(memberId, request);

        // then
        Member member = memberService.findByEmail(하온_이메일);
        assertThat(member.getAuthority()).isEqualTo(Authority.REGULAR_MEMBER);
    }
}
