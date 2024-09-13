package moheng.member.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.auth.application.AuthService;
import moheng.auth.domain.oauth.Authority;
import moheng.config.slice.ServiceTestConfig;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.repository.MemberLiveInformationRepository;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import moheng.member.domain.GenderType;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.dto.request.SignUpInterestTripsRequest;
import moheng.member.dto.request.SignUpLiveInfoRequest;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.member.dto.request.UpdateProfileRequest;
import moheng.member.dto.response.FindMemberAuthorityAndProfileResponse;
import moheng.member.exception.DuplicateNicknameException;
import moheng.member.exception.NoExistMemberException;
import moheng.member.exception.ShortContentidsSizeException;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.trip.application.TripService;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.MemberTripRepository;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;


public class MemberServiceTest extends ServiceTestConfig {
    @Autowired
    private MemberService memberService;

    @Autowired
    private LiveInformationRepository liveInformationRepository;

    @Autowired
    private TripService tripService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberLiveInformationRepository memberLiveInformationRepository;

    @Autowired
    private MemberTripRepository memberTripRepository;

    @DisplayName("회원을 저장한다.")
    @Test
    void 회원을_저장한다() {
        // given
        Member member = new Member(하온_이메일, 하온_소셜_타입_카카오, 하온_프로필_경로);

        // when, then
        assertDoesNotThrow(() -> memberService.save(member));
    }

    @DisplayName("소셜 로그인을 시도한 회원을 저장한다.")
    @Test
    void 소셜_로그인을_시도한_회원을_저장한다() {
        // given
        Member member = new Member(하온_이메일, 하온_소셜_타입_카카오, 하온_프로필_경로);

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

        Member member = new Member(리안_이메일, SocialType.KAKAO, 리안_프로필_경로);
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

        Member member = new Member(래오_이메일, 래오_소셜_타입_카카오, 래오_프로필_경로);
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
        Member member = memberRepository.save(new Member(하온_이메일, 하온_소셜_타입_카카오, 하온_프로필_경로));
        SignUpProfileRequest signUpProfileRequest = new SignUpProfileRequest(하온_닉네임, 하온_생년월일, 하온_성별);

        // when, then
        assertDoesNotThrow(() ->
                memberService.signUpByProfile(member.getId(), signUpProfileRequest));
    }

    @DisplayName("프로필 정보로 회원가입 후 멤버의 권한은 INIT_MEMBER 로 그대로 유지된다.")
    @Test
    void 프로필_정보로_회원가입_후_멤버의_권한은_INIT_MEMBER_로_그대로_유지된다() {
        // given
        Member member = memberRepository.save(new Member(하온_이메일, 하온_소셜_타입_카카오, 하온_프로필_경로));
        SignUpProfileRequest signUpProfileRequest = new SignUpProfileRequest(하온_닉네임, 하온_생년월일, 하온_성별);

        // when
        memberService.signUpByProfile(member.getId(), signUpProfileRequest);

        // then
        Member initMember = memberRepository.findById(member.getId()).get();
        assertEquals(initMember.getAuthority(), Authority.INIT_MEMBER);
    }

    @DisplayName("존재하지 않는 회원의 프로필 정보로 회원가입하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_회원의_프로필_정보로_회원가입하면_예외가_발생한다() {
        // given
        SignUpProfileRequest signUpProfileRequest = new SignUpProfileRequest(하온_닉네임, 하온_생년월일, 하온_성별);

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

        SignUpProfileRequest signUpProfileRequest = new SignUpProfileRequest(래오_닉네임, 하온_생년월일, 하온_성별);

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

        LiveInformation liveInformation1 = liveInformationRepository.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationRepository.save(new LiveInformation("생활정보2"));
        SignUpLiveInfoRequest request = new SignUpLiveInfoRequest(List.of(liveInformation1.getName(), liveInformation2.getName()));

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

    @DisplayName("존재하지 않는 생활정보를 선택하여 회원가입를 시도하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_생활정보를_선택하여_회원가입을_시도하면__예외가_발생한다() {
        // given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();
        SignUpLiveInfoRequest request = new SignUpLiveInfoRequest(List.of("없는 생활정보"));

        // when, then
        assertThatThrownBy(() -> memberService.signUpByLiveInfo(memberId, request))
                .isInstanceOf(NoExistLiveInformationException.class);
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

    @DisplayName("회원이 선택한 관심 여행지 우선순위를 1위부터 시작하여 순차대로 저장한다.")
    @ParameterizedTest
    @MethodSource("관심_여행지_랭크_값을_찾는다")
    void 회원이_선택한_관심_여행지_우선순위를_1위부터_시작하여_순차대로_저장한다(long expectedRank) {
        // given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();

        for (long contentId = 1; contentId <= 5; contentId++) {
            tripService.save(new Trip("롯데월드", "서울특별시 송파구", contentId, "설명", "https://image.com"));
        }
        SignUpInterestTripsRequest request = new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L));

        // when
        memberService.signUpByInterestTrips(memberId, request);

        // then
        assertAll(() -> {
            assertEquals(recommendTripRepository.findById(expectedRank).get().getRanking(), expectedRank);
        });
    }

    @DisplayName("멤버의 여행지를 저장한다.")
    @Test
    void 멤버의_여행지를_저장한다() {
        // given
        Member member = memberRepository.save(하온_기존());

        // when
        for (long contentId = 1; contentId <= 5; contentId++) {
            tripService.save(new Trip("롯데월드", "서울특별시 송파구", contentId, "설명", "https://image.com"));
        }
        SignUpInterestTripsRequest request = new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L));

        // then
        memberService.signUpByInterestTrips(member.getId(), request);

        // then
        assertEquals(memberTripRepository.findByMember(member).size(), 5);
    }

    static Stream<Arguments> 관심_여행지_랭크_값을_찾는다() {
        return Stream.of(
                Arguments.of(1L),
                Arguments.of(2L),
                Arguments.of(3L),
                Arguments.of(4L),
                Arguments.of(5L)
        );
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

    @DisplayName("선택한 관심 여행지중에 존재하지 않는 여행지가 일부 존재한다면 예외가 발생한다.")
    @Test
    void 선택한_관심_여행지중에_존재하지_않는_여행지가_일부_존재한다면_예외가_발생한다() {
        /// given
        memberService.save(하온_기존());
        long memberId = memberService.findByEmail(하온_이메일).getId();

        SignUpInterestTripsRequest request = new SignUpInterestTripsRequest(List.of(-1L, -2L, -3L, -4L, -5L));

        // when, then
        assertThatThrownBy(() -> memberService.signUpByInterestTrips(memberId, request))
                .isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("소셜 로그인 후 최초 회원가입을 마친 멤버의 각 프로필 정보는 null 일 수 없다.")
    @ParameterizedTest
    @MethodSource("멤버의_프로필_정보를_찾는다")
    void 소셜_로그인_후_최초_회원가입을_마친_멤버의_프로필_정보가_null_아님을_확인(Function<Member, Object> fieldExtractor) {
        // given
        authService.generateTokenWithCode("code", "KAKAO");
        Member member = memberRepository.findByEmail("stub@kakao.com").get();
        memberService.signUpByProfile(member.getId(), new SignUpProfileRequest("닉네임", LocalDate.of(2000, 1, 1), GenderType.MEN));
        Member actual = memberRepository.findByEmail("stub@kakao.com").get();

        // when
        Object fieldValue = fieldExtractor.apply(actual);

        // then
        assertThat(fieldValue).isNotNull();
    }

    static Stream<Arguments> 멤버의_프로필_정보를_찾는다() {
        return Stream.of(
                Arguments.of((Function<Member, Object>) Member::getId),
                Arguments.of((Function<Member, Object>) Member::getNickName),
                Arguments.of((Function<Member, Object>) Member::getProfileImageUrl),
                Arguments.of((Function<Member, Object>) Member::getSocialType),
                Arguments.of((Function<Member, Object>) Member::getBirthday),
                Arguments.of((Function<Member, Object>) Member::getGenderType)
        );
    }

    @DisplayName("소셜 로그인 후 최초 회원가입을 마친 멤버의 프로필 정보와 생활정보와 관심 여행지 정보는 비어있을 수 없다.")
    @ParameterizedTest
    @MethodSource("멤버의_프로필_정보와_생활정보_및_관심_여행지_기댓값을_찾는다")
    void 소셜_로그인_후_최초_회원가입을_마친_멤버의_프로필_정보와_생활정보와_관심_여행지_정보는_비어있을_수_없다(Function<Member, Object> fieldExtractor, int expectedLiveInfoSize, int expectedTripSize) {
        // given
        authService.generateTokenWithCode("code", "KAKAO");
        Member member = memberRepository.findByEmail("stub@kakao.com").get();

        // 프로필 회원가입
        memberService.signUpByProfile(member.getId(), new SignUpProfileRequest("닉네임", LocalDate.of(2000, 1, 1), GenderType.MEN));

        // 생활정보 회원가입
        LiveInformation liveInformation1 = liveInformationRepository.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationRepository.save(new LiveInformation("생활정보2"));
        SignUpLiveInfoRequest liveInfoRequest = new SignUpLiveInfoRequest(List.of(liveInformation1.getName(), liveInformation2.getName()));
        memberService.signUpByLiveInfo(member.getId(), liveInfoRequest);

        // 관심 여행지 회원가입
        for (long contentId = 1; contentId <= 5; contentId++) {
            tripService.save(new Trip("롯데월드", "서울특별시 송파구", contentId, "설명", "https://image.com"));
        }
        SignUpInterestTripsRequest interestTripsRequest = new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L));
        memberService.signUpByInterestTrips(member.getId(), interestTripsRequest);

        Member actual = memberRepository.findByEmail("stub@kakao.com").get();

        // when
        Object fieldValue = fieldExtractor.apply(actual);

        // then
        assertAll(() -> {
            assertThat(fieldValue).isNotNull();
            assertEquals(memberLiveInformationRepository.findLiveInformationsByMemberId(member.getId()).size(), expectedLiveInfoSize);
            assertEquals(recommendTripRepository.findAllByMemberId(member.getId()).size(), expectedTripSize);
        });
    }

    static Stream<Arguments> 멤버의_프로필_정보와_생활정보_및_관심_여행지_기댓값을_찾는다() {
        return Stream.of(
                Arguments.of((Function<Member, Object>) Member::getId, 2, 5),
                Arguments.of((Function<Member, Object>) Member::getNickName, 2, 5),
                Arguments.of((Function<Member, Object>) Member::getProfileImageUrl, 2, 5),
                Arguments.of((Function<Member, Object>) Member::getSocialType, 2, 5),
                Arguments.of((Function<Member, Object>) Member::getBirthday, 2, 5),
                Arguments.of((Function<Member, Object>) Member::getGenderType, 2, 5)
        );
    }

    @DisplayName("멤버의 프로필을 수정했을 때 멤버의 모든 프로필 정보는 null 일 수 없으며 생활정보는 변하지 않는다.")
    @ParameterizedTest
    @MethodSource("멤버의_프로필_정보와_생활정보_기댓값을_찾는다")
    void 멤버의_프로필을_수정했을_때_멤버의_모든_프로필_정보는_null_일_수_없으며_멤버의_생활정보는_변하지_않는다(Function<Member, Object> fieldExtractor, int expectedLiveInfoSize) {
        // given
        authService.generateTokenWithCode("code", "KAKAO");
        Member member = memberRepository.findByEmail("stub@kakao.com").get();

        // 프로필 회원가입
        memberService.signUpByProfile(member.getId(), new SignUpProfileRequest("닉네임", LocalDate.of(2000, 1, 1), GenderType.MEN));

        // 생활정보 회원가입
        LiveInformation liveInformation1 = liveInformationRepository.save(new LiveInformation("생활정보1"));
        LiveInformation liveInformation2 = liveInformationRepository.save(new LiveInformation("생활정보2"));
        SignUpLiveInfoRequest liveInfoRequest = new SignUpLiveInfoRequest(List.of(liveInformation1.getName(), liveInformation2.getName()));
        memberService.signUpByLiveInfo(member.getId(), liveInfoRequest);

        // 프로필 수정
        memberService.updateByProfile(member.getId(), new UpdateProfileRequest("수정된 닉네임", LocalDate.of(2000, 1, 1), GenderType.MEN, "profile img url"));

        // then
        Member actual = memberRepository.findByEmail("stub@kakao.com").get();
        Object fieldValue = fieldExtractor.apply(actual);

        // when, then
        assertAll(() -> {
            assertThat(fieldValue).isNotNull();
            assertEquals(memberLiveInformationRepository.findLiveInformationsByMemberId(member.getId()).size(), expectedLiveInfoSize);
        });
    }

    static Stream<Arguments> 멤버의_프로필_정보와_생활정보_기댓값을_찾는다() {
        return Stream.of(
                Arguments.of((Function<Member, Object>) Member::getId, 2),
                Arguments.of((Function<Member, Object>) Member::getNickName, 2),
                Arguments.of((Function<Member, Object>) Member::getProfileImageUrl, 2),
                Arguments.of((Function<Member, Object>) Member::getSocialType, 2),
                Arguments.of((Function<Member, Object>) Member::getBirthday, 2),
                Arguments.of((Function<Member, Object>) Member::getGenderType, 2)
        );
    }

    @DisplayName("멤버의 등급이 정규 회원이라면 프로필 이미지를 리턴한다.")
    @Test
    void 멤버의_등급이_정규_회원이라면_프로필_이미지를_리턴한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        member.changePrivilege(Authority.REGULAR_MEMBER);

        // when
        FindMemberAuthorityAndProfileResponse actual = memberService.findMemberAuthorityAndProfileImg(member.getId());

        // then
        assertThat(actual.getProfileImageUrl()).isNotNull();
    }

    @DisplayName("멤버의 등급이 최초 회원이라면 프로필 이미지가 아닌 null 을 리턴한다.")
    @Test
    void 멤버의_등급이_최초_회원이라면_프로필_이미지가_아닌_null_을_리턴한다() {
        // given
        Member member = memberRepository.save(하온_신규());

        // when
        FindMemberAuthorityAndProfileResponse actual = memberService.findMemberAuthorityAndProfileImg(member.getId());

        // then
        assertThat(actual.getProfileImageUrl()).isNull();
    }
}
