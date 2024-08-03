package moheng.auth.domain;

import static moheng.fixture.MemberFixtures.MEMBER_ID_1;
import static moheng.fixture.MemberFixtures.MEMBER_ID_2;
import static moheng.fixture.MemberFixtures.MEMBER_ID_3;
import static moheng.fixture.MemberFixtures.MEMBER_ID_4;
import static moheng.fixture.MemberFixtures.MEMBER_ID_5;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.auth.domain.token.InMemoryRefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryRefreshTokenRepositoryTest {
    private final InMemoryRefreshTokenRepository refreshTokenRepository = new InMemoryRefreshTokenRepository();

    @DisplayName("리프레시 토큰을 저장한다.")
    @Test
    void 리프레시_토큰을_저장한다() {
        // given
        String refreshToken = "refresh token";

        // when, then
        assertDoesNotThrow(() -> refreshTokenRepository.save(MEMBER_ID_1, refreshToken));
    }

    @DisplayName("유효하지 않은 멤버 id 로 리프레시 토큰을 조회하면 거짓을 리턴한다.")
    @Test
    void 유효하지_않은_멤버_id로_리프레시_토큰을_조회하면_거짓을_리턴한다() {
        // given
        long memberId = -1L;

        //when, then
        assertThat(refreshTokenRepository.existsById(memberId)).isFalse();
    }

    @DisplayName("유효한 멤버 id 로 리프레시 토큰을 조회하면 참을 리턴한다.")
    @Test
    void 유효한_멤버_id로_리프레시_토큰을_조회하면_참을_리턴한다() {
        // given
        String refreshToken = "refresh token";
        refreshTokenRepository.save(MEMBER_ID_2, refreshToken);

        // when
        boolean actual = refreshTokenRepository.existsById(MEMBER_ID_2);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("회원 ID 로 리프레시 토큰을 찾는다.")
    @Test
    void 회원_ID_로_리프레시_토큰을_찾는다() {
        // given
        String refreshToken = "refresh-token";
        refreshTokenRepository.save(MEMBER_ID_3, refreshToken);

        // when, then
        assertEquals(refreshTokenRepository.findById(MEMBER_ID_3), refreshToken);
    }
}
