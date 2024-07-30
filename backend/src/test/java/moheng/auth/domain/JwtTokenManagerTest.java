package moheng.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.auth.exception.NoExistMemberTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtTokenManagerTest {
    private static final String SECRET_KEY = "secret_secret_secret_secret_secret_secret_secret_";
    private static final int ACCESS_TOKEN_EXPIRE_TIME = 3600;
    private static final int REFRESH_TOKEN_EXPIRE_TIME = 3600;

    private final InMemoryRefreshTokenRepository refreshTokenRepository = new InMemoryRefreshTokenRepository();
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, ACCESS_TOKEN_EXPIRE_TIME, REFRESH_TOKEN_EXPIRE_TIME);
    private final JwtTokenManager jwtTokenManager = new JwtTokenManager(refreshTokenRepository, jwtTokenProvider);

    @DisplayName("memberId 로 엑세스 토큰과 리프레시 토큰을 생성한다.")
    @Test
    void memberId_로_엑세스_토큰을_생성한다() {
        // given
        long memberId = 1L;

        // when
        MemberToken memberToken = jwtTokenManager.createMemberToken(memberId);

        // then
        assertAll(
                () -> assertThat(memberToken.getAccessToken()).isNotEmpty(),
                () -> assertThat(memberToken.getRefreshToken()).isNotEmpty()
        );
    }

    @DisplayName("리프레시 토큰 저장소에 없는 리프레시 토큰으로 새로운 엑세스 토큰 발급을 요청하면 예외가 발생한다.")
    @Test
    void 리프레시_토큰_저장소에_없는_리프레시_토큰으로_새로운_엑세스_토큰_발급을_요청하면_예외가_발생한다() {
        // given
        String refreshToken = jwtTokenProvider.createToken("2", 3600);

        // when, then
        assertThatThrownBy(() -> jwtTokenManager.generateRenewalAccessToken(refreshToken))
                .isInstanceOf(NoExistMemberTokenException.class);
    }

    @DisplayName("getMemberId 메소드는 입력받은 토큰의 payload 를 읽어내어 memberId 를 추출한다.")
    @Test
    void getMemberId_메소드는_입력받은_토큰의_payload_를_읽어내어_memberId_를_추출한다() {
        // given
        long expected = 1L;
        String token = jwtTokenProvider.createToken(String.valueOf(expected), 3600);

        // when
        long memberId = jwtTokenManager.getMemberId(token);

        // then
        assertThat(memberId).isEqualTo(expected);
    }

    @DisplayName("리프레시 토큰 저장소에 저장된 토큰을 제거한다.")
    @Test
    void 리프레시_토큰_저장소에_저장된_토큰을_제거한다() {
        // given
        MemberToken memberToken = jwtTokenManager.createMemberToken(1L);
        String refreshToken = memberToken.getRefreshToken();

        // when, then
        assertDoesNotThrow(() -> jwtTokenManager.removeRefreshToken(refreshToken));
    }

    @DisplayName("리프레시 토큰 저장소에 존재하지 않는 토큰을 삭제하면 예외가 발생한다.")
    @Test
    void 리프레시_토큰_저장소에_존재하지_않는_토큰을_삭제하면_예외가_발생한다() {
        // given
        String refreshToken = jwtTokenProvider.createRefreshToken(1);

        // when, then
        assertThatThrownBy(() -> jwtTokenManager.removeRefreshToken(refreshToken))
                .isInstanceOf(NoExistMemberTokenException.class);
    }
}
