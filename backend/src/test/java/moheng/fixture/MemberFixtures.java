package moheng.fixture;

import moheng.member.domain.GenderType;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import moheng.member.dto.request.CheckDuplicateNicknameRequest;
import moheng.member.dto.request.SignUpLiveInfoRequest;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.member.dto.request.UpdateProfileRequest;
import moheng.member.dto.response.MemberResponse;

import java.time.LocalDate;
import java.util.List;

public class MemberFixtures {
    // memberId
    public static final long MEMBER_ID_1 = 1L;
    public static final long MEMBER_ID_2 = 2L;
    public static final long MEMBER_ID_3 = 3L;
    public static final long MEMBER_ID_4 = 4L;
    public static final long MEMBER_ID_5 = 5L;
    public static final long MEMBER_ID_6 = 6L;
    public static final long MEMBER_ID_7 = 7L;
    public static final long MEMBER_ID_8 = 8L;

    // 하온
    public static final String 하온_이메일 = "haon1234@kakao.com";
    public static final String 하온_닉네임 = "haon";
    public static final String 하온_프로필_경로 = "/haon.png";
    public static final SocialType 하온_소셜_타입_카카오 = SocialType.KAKAO;
    public static final SocialType 하온_소셜_타입_구글 = SocialType.GOOGLE;
    public static final LocalDate 하온_생년월일 = LocalDate.of(1999, 9, 20);
    public static final GenderType 하온_성별 = GenderType.MEN;

    public static Member 하온_기존() {
        return new Member(1L, 하온_이메일, 하온_닉네임, 하온_프로필_경로, 하온_소셜_타입_카카오, 하온_생년월일, 하온_성별);
    }

    public static Member 하온_신규() {
        return new Member(하온_이메일, 하온_소셜_타입_카카오);
    }

    public static MemberResponse 하온_응답() {
        return new MemberResponse(하온_기존());
    }

    // 리안
    public static final String 리안_이메일 = "lian1234@kakao.com";
    public static final String 리안_닉네임 = "lian";
    public static final String 리안_프로필_경로 = "/lian.png";
    public static final SocialType 리안_소셜_타입_카카오 = SocialType.KAKAO;
    public static final SocialType 리안_소셜_타입_구글 = SocialType.GOOGLE;
    public static final LocalDate 리안_생년월일 = LocalDate.of(2000, 1, 1);
    public static final GenderType 리안_성별 = GenderType.WOMEN;

    public static Member 리안_신규() {
        return new Member(2L, 리안_이메일, 리안_닉네임, 리안_프로필_경로, 리안_소셜_타입_카카오, 리안_생년월일, 리안_성별);
    }

    public static Member 리안_기존() {
        return new Member(리안_이메일, 리안_소셜_타입_카카오);
    }

    // 레오
    public static final String 래오_이메일 = "leo1234@kakao.com";
    public static final String 래오_닉네임 = "leo";
    public static final String 래오_프로필_경로 = "/leo.png";
    public static final SocialType 래오_소셜_타입_카카오 = SocialType.KAKAO;
    public static final SocialType 래오_소셜_타입_구글 = SocialType.GOOGLE;
    public static final LocalDate 래오_생년월일 = LocalDate.of(2000, 1, 1);
    public static final GenderType 래오_성별 = GenderType.MEN;

    public static Member 래오_기존() {
        return new Member(3L, 래오_이메일, 래오_닉네임, 래오_프로필_경로, 래오_소셜_타입_카카오, 래오_생년월일, 래오_성별);
    }

    public static Member 래오_신규() {
        return new Member(래오_이메일, 래오_소셜_타입_카카오);
    }

    // 스텁(가짜) 회원
    public static final String 스텁_이메일 = "stub@kakao.com";

    // 회원가입 요청
    public static SignUpProfileRequest 프로필_정보로_회원가입_요청() {
        return new SignUpProfileRequest("devhaon", LocalDate.of(1999, 9, 20), GenderType.MEN, "https://profile-image.com");
    }

    public static SignUpLiveInfoRequest 생활정보로_회원가입_요청() {
        return new SignUpLiveInfoRequest(List.of("생활정보1", "생활정보2"));
    }

    public static SignUpLiveInfoRequest 비어있는_생활정보로_회원가입_요청() {
        return new SignUpLiveInfoRequest(List.of());
    }

    // 닉네임 중복확인 요청
    public static CheckDuplicateNicknameRequest 닉네임_중복확인_요청() {
        return new CheckDuplicateNicknameRequest("devhaon");
    }

    // 프로필 업데이트 요청
    public static UpdateProfileRequest 프로필_업데이트_요청() {
        return new UpdateProfileRequest("devhaon", LocalDate.of(1999, 9, 20), GenderType.MEN, "https://profile-image.com");
    }
}
