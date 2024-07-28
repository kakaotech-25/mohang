package moheng.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryRefreshTokenRepositoryTest {
    private final InMemoryRefreshTokenRepository refreshTokenRepository = new InMemoryRefreshTokenRepository();

    @DisplayName("리프레시 토큰을 저장한다.")
    @Test
    void 리프레시_토큰을_저장한다() {
        // given
        long memberId = 1L;
        String refreshToken = "refresh token";

        // when, then
        assertDoesNotThrow(() -> refreshTokenRepository.save(memberId, refreshToken));
    }

    @DisplayName("유효하지 않은 멤버 id 로 리프레시 토큰을 조회하면 거짓을 리턴한다.")
    @Test
    void 유효하지_않은_멤버_id로_토큰을_조회하면_거짓을_리턴한다() {
        // given
        long memberId = -1L;

        //when, then
        assertThat(refreshTokenRepository.existsById(memberId)).isFalse();
    }
}
