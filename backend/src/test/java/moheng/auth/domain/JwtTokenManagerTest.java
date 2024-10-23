package moheng.auth.domain;

import static moheng.fixture.JwtTokenFixtures.*;
import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.auth.domain.token.*;
import moheng.auth.exception.InvalidTokenException;
import moheng.auth.exception.NoExistMemberTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JwtTokenManagerTest {
    private final InMemoryRefreshTokenRepository refreshTokenRepository = new InMemoryRefreshTokenRepository();
    private final JwtTokenGenerator jwtTokenProvider = new JwtTokenGenerator(SECRET_KEY, ACCESS_TOKEN_EXPIRE_TIME, REFRESH_TOKEN_EXPIRE_TIME);
    private final JwtTokenManager jwtTokenManager = new JwtTokenManager(refreshTokenRepository, jwtTokenProvider);

    @DisplayName("memberId로 엑세스 토큰과 리프레시 토큰을 생성한다.")
    @ParameterizedTest
    @ValueSource(longs = {MEMBER_ID_1, MEMBER_ID_2, MEMBER_ID_3})
    void memberId_로_엑세스_토큰과_리프레시_토큰을_생성한다(long memberId) {
        // given, when
        MemberToken memberToken = jwtTokenManager.createMemberToken(memberId);

        // then
        assertAll(
                () -> assertThat(memberToken.getAccessToken()).isNotEmpty(),
                () -> assertThat(memberToken.getRefreshToken()).isNotEmpty()
        );
    }

    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급받는다.")
    @Test
    void 리프레시_토큰으로_새로운_엑세스_토큰을_발급받는다() {
        // given
        MemberToken memberToken = jwtTokenManager.createMemberToken(MEMBER_ID_6);
        String refreshToken = memberToken.getRefreshToken();

        // when
        RenewalToken renewalToken = jwtTokenManager.generateRenewalAccessToken(refreshToken);
        String accessToken = renewalToken.getAccessToken();

        // then
        assertThat(accessToken).isNotEmpty();
    }

    @DisplayName("리프레시 토큰 저장소에 없는 리프레시 토큰으로 새로운 엑세스 토큰 발급을 요청하면 예외가 발생한다.")
    @Test
    void 리프레시_토큰_저장소에_없는_리프레시_토큰으로_새로운_엑세스_토큰_발급을_요청하면_예외가_발생한다() {
        // given
        String refreshToken = jwtTokenProvider.createToken(String.valueOf(MEMBER_ID_3), 3600);

        // when, then
        assertThatThrownBy(() -> jwtTokenManager.generateRenewalAccessToken(refreshToken))
                .isInstanceOf(NoExistMemberTokenException.class);
    }

    @DisplayName("getMemberId 메소드는 입력받은 토큰의 payload 를 읽어내어 memberId 를 추출한다.")
    @Test
    void getMemberId_메소드는_입력받은_토큰의_payload_를_읽어내어_memberId_를_추출한다() {
        // given
        String token = jwtTokenProvider.createToken(String.valueOf(MEMBER_ID_4), 3600);

        // when
        long memberId = jwtTokenManager.getMemberId(token);

        // then
        assertThat(memberId).isEqualTo(MEMBER_ID_4);
    }

    @DisplayName("리프레시 토큰 저장소에 저장된 토큰을 제거한다.")
    @Test
    void 리프레시_토큰_저장소에_저장된_토큰을_제거한다() {
        // given
        MemberToken memberToken = jwtTokenManager.createMemberToken(MEMBER_ID_5);
        String refreshToken = memberToken.getRefreshToken();

        // when, then
        assertDoesNotThrow(() -> jwtTokenManager.removeRefreshToken(refreshToken));
    }

    @DisplayName("리프레시 토큰 저장소에 존재하지 않는 토큰을 삭제하면 예외가 발생한다.")
    @Test
    void 리프레시_토큰_저장소에_존재하지_않는_토큰을_삭제하면_예외가_발생한다() {
        // given
        String refreshToken = jwtTokenProvider.createRefreshToken(-1L);

        // when, then
        assertThatThrownBy(() -> jwtTokenManager.removeRefreshToken(refreshToken))
                .isInstanceOf(NoExistMemberTokenException.class);
    }

    @DisplayName("전달받은 리프레시 토큰이 만료되었다면 예외가 발생한다.")
    @Test
    void 전달받은_리프레시_토큰이_만료되었다면_예외가_발생한다() {
        // given
        JwtTokenGenerator expiredJwtTokenProvider
                = new JwtTokenGenerator(SECRET_KEY, 0, 0);
        InMemoryRefreshTokenRepository testRefreshTokenRepository
                = new InMemoryRefreshTokenRepository();
        JwtTokenManager testJwtTokenManager
                = new JwtTokenManager(testRefreshTokenRepository, expiredJwtTokenProvider);

        String expiredRefreshToken = expiredJwtTokenProvider.createRefreshToken(MEMBER_ID_1);
        testRefreshTokenRepository.save(MEMBER_ID_1, expiredRefreshToken);

        // when, then
        assertThatThrownBy(() -> testJwtTokenManager.generateRenewalAccessToken(expiredRefreshToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("전달받은 리프레시 토큰과 DB 에 저장된 회원의 리프레시 토큰이 다르면 예외가 발생한다.")
    @Test
    void 전달받은_리프레시_토큰과_DB_에_저장된_회원의_리프레시_토큰이_다르면_예외가_발생한다() {
        // given
        jwtTokenManager.createMemberToken(MEMBER_ID_5);
        String otherRefreshToken = jwtTokenProvider.createRefreshToken(MEMBER_ID_8);

        // when, then
        assertThatThrownBy(() -> jwtTokenManager.generateRenewalAccessToken(otherRefreshToken))
                .isInstanceOf(NoExistMemberTokenException.class);
    }
}
