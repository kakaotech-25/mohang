package moheng.member.domain;

import static moheng.fixture.MemberFixtures.*;
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
        Member 래오 = memberRepository.save(래오_신규());

        // when
        Member actual = memberRepository.findByEmail(래오.getEmail()).get();

        // then
        assertThat(래오.getId()).isEqualTo(actual.getId());
    }

    @DisplayName("중복된 이메일이 존재하면 참을 리턴한다.")
    @Test
    void 중복된_이메일이_존재하면_참을_리턴한다() {
        // given
        Member 리안 = memberRepository.save(리안_신규());

        // when & then
        assertTrue(memberRepository.existsByEmail(리안.getEmail()));
    }

    @DisplayName("중복된 닉네임이 존재하면 참을 리턴한다.")
    @Test
    void 중복된_닉네임이_존재하면_참을_리턴한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());

        // when & then
        assertThat(memberRepository.existsByNickName(하온.getNickName())).isTrue();
    }

    @DisplayName("존재하지 않는 회원을 찾으면 거짓을 리턴한다.")
    @Test
    void 존재하지_않는_회원을_찾으면_거짓을_리턴한다() {
        // given
        long 존재하지_않는_회원_ID = -1L;

        // when & then
        assertThat(memberRepository.existsById(존재하지_않는_회원_ID)).isFalse();
    }
}
