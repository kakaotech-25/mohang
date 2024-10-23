package moheng.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static moheng.fixture.JwtTokenFixtures.INVALID_REFRESH_TOKEN;
import static moheng.fixture.MemberFixtures.MEMBER_ID_1;
import static moheng.fixture.MemberFixtures.스텁_이메일;
import static moheng.fixture.AuthFixtures.AUHTORIZATION_CODE;
import static moheng.fixture.AuthFixtures.KAKAO_PROVIDER_NAME;

import moheng.auth.domain.token.JwtTokenManager;
import moheng.auth.domain.token.JwtTokenGenerator;
import moheng.auth.domain.token.MemberToken;
import moheng.auth.dto.RenewalAccessTokenRequest;
import moheng.auth.dto.RenewalAccessTokenResponse;
import moheng.auth.exception.InvalidTokenException;
import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class AuthServiceTest extends ServiceTestConfig {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private JwtTokenGenerator jwtTokenProvider;


    @DisplayName("카카오 로그인을 위한 링크를 생성한다.")
    @Test
    void 카카오_로그인을_위한_링크를_생성한다() {
        // given
        String link = authService.generateUri(KAKAO_PROVIDER_NAME);

        // when, then
        assertThat(link).isNotEmpty();
    }

    @DisplayName("토큰 생성을 하면 OAuth 서버에서 인증 후 토큰을 반환한다")
    @Test
    void 토큰_생성을_하면_OAuth_서버에서_인증_후_토큰을_반환한다() {
        // given

        // when
        MemberToken actual = authService.generateTokenWithCode(AUHTORIZATION_CODE, KAKAO_PROVIDER_NAME);

        // then
        assertThat(actual.getAccessToken()).isNotEmpty();
    }

    @DisplayName("Authorization Code 를 전달받으면 회원 정보가 데이터베이스에 저장된다.")
    @Test
    void Authorization_Code_를_전달받으면_회원_정보가_데이터베이스에_저장된다() {
        // given
        authService.generateTokenWithCode(AUHTORIZATION_CODE, KAKAO_PROVIDER_NAME);

        // when
        boolean actual = memberRepository.existsByEmail(스텁_이메일);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("이미 가입된 회원에 대한 Authorization Code를 전달받으면 추가로 유저가 생성되지 않는다")
    @Test
    void 이미_가입된_회원에_대한_Authorization_Code를_전달받으면_유저가_추가로_생성되지_않는다() {
        // given
        authService.generateTokenWithCode(AUHTORIZATION_CODE, KAKAO_PROVIDER_NAME);

        // when, then
        authService.generateTokenWithCode(AUHTORIZATION_CODE, KAKAO_PROVIDER_NAME);
        authService.generateTokenWithCode(AUHTORIZATION_CODE, KAKAO_PROVIDER_NAME);
        authService.generateTokenWithCode(AUHTORIZATION_CODE, KAKAO_PROVIDER_NAME);
        List<Member> actual = memberRepository.findAll();

        // then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("Authorization Code 로 토큰을 생성시 리프레시 토큰, 엑세스 토큰을 모두 발급한다.")
    @Test
    void Authorization_Code_로_토큰을_생성시_리프레시_토큰_엑세스_토큰을_모두_발급한다() {
        // given, when
        MemberToken actual = authService.generateTokenWithCode(AUHTORIZATION_CODE, KAKAO_PROVIDER_NAME);

        // then
        assertThat(actual.getAccessToken()).isNotEmpty();
        assertThat(actual.getRefreshToken()).isNotEmpty();
    }

    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급받는다.")
    @Test
    void 리프레시_토큰으로_새로운_엑세스_토큰을_발급받는다() {
        // given
        MemberToken memberToken = jwtTokenManager.createMemberToken(5L);
        String refreshToken = memberToken.getRefreshToken();
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
        MemberToken memberToken = jwtTokenManager.createMemberToken(MEMBER_ID_1);
        String refreshToken = memberToken.getRefreshToken();

        RenewalAccessTokenRequest renewalAccessTokenRequest
                = new RenewalAccessTokenRequest(refreshToken);
        RenewalAccessTokenResponse renewalAccessTokenResponse
                = authService.generateRenewalAccessToken(renewalAccessTokenRequest);

        // when, then
        assertThat(renewalAccessTokenResponse.getAccessToken()).isNotEmpty();
    }

    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급시, 리프레시 토큰이 유효하지 않다면 예외를 던진다.")
    @Test
    void 리프레시_토큰으로_새로운_엑세스_토큰을_발급_할_때_리프레시_토큰이_유효하지_않으면_예외를_던진다() {
        // given
        RenewalAccessTokenRequest renewalAccessTokenRequest = new RenewalAccessTokenRequest(INVALID_REFRESH_TOKEN);

        // when, then
        assertThatThrownBy(() -> authService.generateRenewalAccessToken(renewalAccessTokenRequest))
                .isInstanceOf(InvalidTokenException.class);
    }

    @DisplayName("존재하지 않는 멤버에 대한 엑세스 토큰을 추출하려고 하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버에_대한_엑세스_토큰을_추출하려고_하면_예외가_발생한다() {
        // given
        long 없는_멤버_ID = -1L;
        String accessToken = jwtTokenProvider.createAccessToken(없는_멤버_ID);

        // when, then
        assertThatThrownBy(() -> authService.extractMemberId(accessToken))
                .isInstanceOf(NoExistMemberException.class);
    }
}