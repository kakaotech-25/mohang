package moheng.fixture;

import moheng.member.domain.GenderType;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;

import java.time.LocalDate;

public class MemberFixtures {
    public static final String 하온_이메일 = "haon1234@kakao.com";
    public static final String 하온_닉네임 = "haon";
    public static final String 하온_프로필_경로 = "/haon.png";
    public static final SocialType 하온_소셜_타입_카카오 = SocialType.KAKAO;
    public static final LocalDate 하온_생년월일 = LocalDate.of(1999, 9, 20);
    public static final GenderType 하온_성별 = GenderType.MEN;

    public static Member 하온_신규() {
        return new Member(1L, 하온_이메일, 하온_닉네임, 하온_프로필_경로, 하온_소셜_타입_카카오, 하온_생년월일, 하온_성별);
    }

    public static Member 하온_기존() {
        return new Member(하온_이메일, 하온_소셜_타입_카카오);
    }
}
