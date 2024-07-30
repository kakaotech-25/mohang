package moheng.auth.application;

import moheng.auth.domain.JwtTokenProvider;
import moheng.auth.domain.MemberToken;
import moheng.auth.dto.RenewalAccessTokenRequest;
import moheng.auth.dto.RenewalAccessTokenResponse;
import moheng.auth.dto.TokenResponse;
import moheng.auth.exception.InvalidTokenException;
import moheng.config.ServiceTestConfig;
import moheng.config.TestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = TestConfig.class)
class AuthServiceTest extends ServiceTestConfig {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @DisplayName("카카오 로그인을 위한 링크를 생성한다.")
    @Test
    void 카카오_로그인을_위한_링크를_생성한다() {
        // given
        String code = "authorization_code";
        String link = authService.generateUri();

        // when, then
        assertThat(link).isNotEmpty();
    }

    @DisplayName("토큰 생성을 하면 OAuth 서버에서 인증 후 토큰을 반환한다")
    @Test
    void 토큰_생성을_하면_OAuth_서버에서_인증_후_토큰을_반환한다() {
        // given
        String code = "authorization code";
        String oAuthProvider = "kakao";

        // when
        MemberToken actual = authService.generateTokenWithCode(code, oAuthProvider);

        // then
        assertThat(actual.getAccessToken()).isNotEmpty();
    }

    @DisplayName("Authorization Code 를 전달받으면 회원 정보가 데이터베이스에 저장된다.")
    @Test
    void Authorization_Code_를_전달받으면_회원_정보가_데이터베이스에_저장된다() {
        // given
        String code = "authorization code";
        String oAuthProvider = "kakao";
        authService.generateTokenWithCode(code, oAuthProvider);

        // when
        boolean actual = memberRepository.existsByEmail("stub@naver.com");

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이미 가입된 회원에 대한 Authorization Code를 전달받으면 추가로 유저가 생성되지 않는다")
    @Test
    void 이미_가입된_회원에_대한_Authorization_Code를_전달받으면_유저가_추가로_생성되지_않는다() {
        // given
        String code = "authorization code";
        String oAuthProvider = "kakao";
        authService.generateTokenWithCode(code, oAuthProvider);

        // when, then
        authService.generateTokenWithCode(code, oAuthProvider);
        authService.generateTokenWithCode(code, oAuthProvider);
        authService.generateTokenWithCode(code, oAuthProvider);
        List<Member> actual = memberRepository.findAll();

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("Authorization Code 로 토큰을 생성시 리프레시 토큰, 엑세스 토큰을 모두 발급한다.")
    @Test
    void Authorization_Code_로_토큰을_생성시_리프레시_토큰_엑세스_토큰을_모두_발급한다() {
        // given
        String code = "authorization code";
        String oAuthProvider = "kakao";

        // when
        MemberToken actual = authService.generateTokenWithCode(code, oAuthProvider);

        // then
        assertThat(actual.getAccessToken()).isNotEmpty();
        assertThat(actual.getRefreshToken()).isNotEmpty();
    }

    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급받는다.")
    @Test
    void 리프레시_토큰으로_새로운_엑세스_토큰을_발급받는다() {
        // given
        String refreshToken = jwtTokenProvider.createRefreshToken(3L);
        RenewalAccessTokenRequest renewalAccessTokenRequest
                = new RenewalAccessTokenRequest(refreshToken);

        // when
        RenewalAccessTokenResponse renewalAccessTokenResponse
                = authService.generateRenewalAccessToken(renewalAccessTokenRequest);

        // then
        assertThat(renewalAccessTokenResponse.getAccessToken()).isNotEmpty();
    }

    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급받는다.")
    @Test
    void 리프레시_토큰으로_새로운_엑세스_토큰을_갱신한다() {
        // given
        String testRefreshToken = jwtTokenProvider.createRefreshToken(10L);

        RenewalAccessTokenRequest renewalAccessTokenRequest
                = new RenewalAccessTokenRequest(testRefreshToken);
        RenewalAccessTokenResponse renewalAccessTokenResponse
                = authService.generateRenewalAccessToken(renewalAccessTokenRequest);

        // when, then
        assertThat(renewalAccessTokenResponse.getAccessToken()).isNotEmpty();
    }

    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급시, 리프레시 토큰이 유효하지 않다면 예외를 던진다.")
    @Test
    void 리프레시_토큰으로_새로운_엑세스_토큰을_발급_할_때_리프레시_토큰이_유효하지_않으면_예외를_던진다() {
        // given
        String testRefreshToken = "invalid-refresh-token";
        RenewalAccessTokenRequest renewalAccessTokenRequest = new RenewalAccessTokenRequest(testRefreshToken);

        // when, then
        assertThatThrownBy(() -> authService.generateRenewalAccessToken(renewalAccessTokenRequest))
                .isInstanceOf(InvalidTokenException.class);
    }
}