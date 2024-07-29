package moheng.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.config.RepositoryTestConfig;
import moheng.config.TestConfig;
import moheng.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = TestConfig.class)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일을 통해 회원을 찾는다.")
    @Test
    void 이메일을_통해_회원을_찾는다() {
        // given
        String email = "msung6924@naver.com";
        String nickname = "msung99";
        String profile_image_url = "https://msung99";
        Member member = new Member(email, SocialType.KAKAO);
        Member savedMember = memberRepository.save(member);

        // when
        Member foundMember = memberRepository.findByEmail(email).get();

        // then
        assertThat(savedMember.getId()).isEqualTo(foundMember.getId());
    }

    @DisplayName("중복된 이메일이 존재하면 참을 리턴한다.")
    @Test
    void 중복된_이메일이_존재하면_true를_반환한다() {
        // given
        String email = "msung6924@naver.com";
        memberRepository.save(new Member(email, SocialType.KAKAO));

        // when & then
        assertThat(memberRepository.existsByEmail(email)).isTrue();
    }
}
