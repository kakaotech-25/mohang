package moheng.member.domain;

import static moheng.fixture.MemberFixtures.래오_이메일;
import static moheng.fixture.MemberFixtures.래오_소셜_타입_카카오;
import static moheng.fixture.MemberFixtures.리안_이메일;
import static moheng.fixture.MemberFixtures.리안_소셜_타입_카카오;
import static moheng.fixture.MemberFixtures.하온_이메일;
import static moheng.fixture.MemberFixtures.하온_프로필_경로;
import static moheng.fixture.MemberFixtures.하온_소셜_타입_카카오;
import static moheng.fixture.MemberFixtures.하온_생년월일;
import static moheng.fixture.MemberFixtures.하온_성별;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import moheng.config.slice.RepositoryTestConfig;
import moheng.config.TestConfig;
import moheng.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



public class MemberRepositoryTest extends RepositoryTestConfig {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("이메일을 통해 회원을 찾는다.")
    @Test
    void 이메일을_통해_회원을_찾는다() {
        // given
        Member member = new Member(래오_이메일, 래오_소셜_타입_카카오);
        Member savedMember = memberRepository.save(member);

        // when
        Member foundMember = memberRepository.findByEmail(래오_이메일).get();

        // then
        assertThat(savedMember.getId()).isEqualTo(foundMember.getId());
    }

    @DisplayName("중복된 이메일이 존재하면 참을 리턴한다.")
    @Test
    void 중복된_이메일이_존재하면_참을_리턴한다() {
        // given
        memberRepository.save(new Member(리안_이메일, 리안_소셜_타입_카카오));

        // when & then
        assertTrue(memberRepository.existsByEmail(리안_이메일));
    }

    @DisplayName("중복된 닉네임이 존재하면 참을 리턴한다.")
    @Test
    void 중복된_닉네임이_존재하면_참을_리턴한다() {
        // given
        String nickname = "msung99";
        Member member = new Member(1L, 하온_이메일, nickname,
                하온_프로필_경로, 하온_소셜_타입_카카오, 하온_생년월일, 하온_성별);
        memberRepository.save(member);

        // when & then
        assertThat(memberRepository.existsByNickName(nickname)).isTrue();
    }

    @DisplayName("존재하지 않는 회원을 찾으면 거짓을 리턴한다.")
    @Test
    void 존재하지_않는_회원을_찾으면_거짓을_리턴한다() {
        // given
        Long id = 0L;

        // when & then
        assertThat(memberRepository.existsById(id)).isFalse();
    }
}
