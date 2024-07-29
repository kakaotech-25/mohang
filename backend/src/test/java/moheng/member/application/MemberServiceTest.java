package moheng.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.config.ServiceTestConfig;
import moheng.member.domain.GenderType;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class MemberServiceTest extends ServiceTestConfig {
    @Autowired
    private MemberService memberService;

    @DisplayName("소셜 로그인을 시도한 회원을 저장한다.")
    @Test
    void 소셜_로그인을_시도한_회원을_저장한다() {
        // given
        Member member = new Member("msung6924@naver.com", SocialType.KAKAO);

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

        Member member = new Member(email, SocialType.KAKAO);
        memberService.save(member);

        // when
        Member foundMember = memberService.findByEmail(email);

        // then
        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @DisplayName("주어진 이메일로 가입된 회원이 존재하는지 확인한다.")
    @Test
    void 주어진_이메일로_가입된_회원이_존재하는지_확인한다() {
        // given
        String email = "msung6924@naver.com";
        String nickname = "msung99";
        String profileImageUrl = "https://image";

        Member member = new Member(email, SocialType.KAKAO);
        memberService.save(member);

        // when
        boolean actual = memberService.existsByEmail(email);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이미 중복되는 닉네임이 존재한다면 참을 리턴한다.")
    @Test
    public void 이미_존재하는_닉네임이_존재한다면_참을_리턴한다() {
        // given
        String email = "msung6924@naver.com";
        String nickname = "msung99";
        String profileImageUrl = "https://image";
        Member member = new Member(2L, email, nickname,
                profileImageUrl, SocialType.KAKAO,
                LocalDate.of(2000, 1, 1), GenderType.MEN);
        memberService.save(member);

        // when, then
        boolean actual = memberService.existsByNickname(nickname);

       assertThat(actual).isTrue();
    }
}
