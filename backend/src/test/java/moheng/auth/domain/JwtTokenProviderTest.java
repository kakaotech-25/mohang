package moheng.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.auth.exception.InvalidTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtTokenProviderTest {
    private static final String SECRET_KEY = "secret_secret_secret_secret_secret_secret_secret_";
    private static final int ACCESS_TOKEN_EXPIRE_TIME = 3600;
    private static final int REFRESH_TOKEN_EXPIRE_TIME = 3600;
    private static final int EXPIRED_TOKEN_TIME = 0;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            new InMemoryRefreshTokenRepository(), SECRET_KEY,
            ACCESS_TOKEN_EXPIRE_TIME, REFRESH_TOKEN_EXPIRE_TIME
    );

    @DisplayName("엑세스 토큰을 생성한다.")
    @Test
    void 엑세스_토큰을_생성한다() {
        // given, when
        String actual = jwtTokenProvider.createAccessToken(1L);

        // then
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("리프레시 토큰을 생성한다.")
    @Test
    void 리프레시_토큰을_생성한다() {
        // given, when
        String actual = jwtTokenProvider.createRefreshToken(1L);

        // then
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("JWT 토큰의 Payload를 가져온다.")
    @Test
    void JWT_토큰의_Payload를_가져온다() {
        // given
        String expected = "payload";
        String token = jwtTokenProvider.createToken(expected, ACCESS_TOKEN_EXPIRE_TIME);

        // when
        String actual = jwtTokenProvider.getPayload(token);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("만료된 토큰을 전달받으면 예외가 발생한다.")
    @Test
    void 만료된_토큰을_전달받으면_예외가_발생한다() {
        // given
        JwtTokenProvider expiredJwtTokenProvider = new JwtTokenProvider(
                new InMemoryRefreshTokenRepository(), SECRET_KEY,
                ACCESS_TOKEN_EXPIRE_TIME, REFRESH_TOKEN_EXPIRE_TIME
        );
        String expiredToken = expiredJwtTokenProvider.createToken("payload", EXPIRED_TOKEN_TIME);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("만료된 리프레시 토큰을 전달받으면 예외를 발생시킨다.")
    @Test
    void 만료된_리프레시_토큰을_전달받으면_예외를_발생시킨다() {
        // given
        JwtTokenProvider expiredJwtTokenProvider = new JwtTokenProvider(
                new InMemoryRefreshTokenRepository(), SECRET_KEY,
                EXPIRED_TOKEN_TIME, EXPIRED_TOKEN_TIME
        );
        String expiredToken = expiredJwtTokenProvider.createRefreshToken(1L);

        // when, then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("만료된 엑세스 토큰을 전달받으면 예외를 발생시킨다.")
    @Test
    void 만료된_엑세스_토큰을_전달받으면_예외를_발생시킨다() {
        // given
        JwtTokenProvider expiredJwtTokenProvider = new JwtTokenProvider(
                new InMemoryRefreshTokenRepository(), SECRET_KEY,
                EXPIRED_TOKEN_TIME, EXPIRED_TOKEN_TIME
        );
        String expiredToken = expiredJwtTokenProvider.createAccessToken(1L);

        // when, then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("변조되었거나 문제가 발생한 토큰을 전달받으면 예외가 발생한다.")
    @Test
    void 변조되었거나_문제가_발생한_토큰을_전달받으면_예외가_발생한다() {
        // given
        String invalidToken = "invalid_token";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(invalidToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("리프레시 토큰 저장소에 유저의 토큰이 존재하지 않으면 새로운 토큰을 생성하고 저장한다.")
    @Test
    void a() {
        // given
        long newMemberId = 1L;
        InMemoryRefreshTokenRepository refreshTokenRepository = new InMemoryRefreshTokenRepository();
        JwtTokenProvider tokenProvider = new JwtTokenProvider(
                refreshTokenRepository,  SECRET_KEY,
                ACCESS_TOKEN_EXPIRE_TIME, REFRESH_TOKEN_EXPIRE_TIME
        );

        // when
        tokenProvider.createRefreshToken(newMemberId);

        // then
        assertThat(refreshTokenRepository.findById(newMemberId)).isNotEmpty();
    }
}
