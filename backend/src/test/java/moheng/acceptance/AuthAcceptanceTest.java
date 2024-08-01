package moheng.acceptance;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.*;
import static moheng.acceptance.fixture.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTestConfig {

    @DisplayName("카카오 소셜 로그인을 위한 Authorization URI 를 생성한다")
    @Test
    void 카카오_소셜_로그인을_위한_Authorization_URI_를_생성한다() {
        // given, when
        ExtractableResponse<Response> response = OAuth_인증_URI를_생성한다("KAKAO");
        OAuthUriResponse oAuthUriResponse = response.as(OAuthUriResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(oAuthUriResponse.getoAuthUri()).contains("https://");
        });
    }


    @DisplayName("최초 사용자나 기존에 존재하는 회원이 다시 로그인하는 경우 토큰을 발급하고 상태코드 201을 리턴한다.")
    @Test
    void 최초_사용자나_기존에_존재하는_회원이_다시_로그인하는_경우_토큰을_발급하고_상태코드_200을_리턴한다() {
        // given, when
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse tokenResponse = response.as(AccessTokenResponse.class);

        // then
        assertAll(() -> {
            상태코드_201이_반환된다(response);
            assertThat(tokenResponse.getAccessToken()).isNotEmpty();
        });
    }

    @DisplayName("리프레시 토큰을 통해 새로운 엑세스 토큰을 발급받고 상태코드 201을 리턴한다.")
    @Test
    public void 리프레시_토큰을_통해_새로운_엑세스_토큰을_발급받고_상태코드_201을_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        final String refreshToken = response.headers().getValue("Set-Cookie");

        // when
        ExtractableResponse<Response> actual = 리프레시_토큰을_통해_새로운_엑세스_토큰을_재발급_한다(refreshToken);
        RenewalAccessTokenResponse renewalAccessTokenResponse = actual.as(RenewalAccessTokenResponse.class);

        // then
        assertAll(() -> {
            상태코드_201이_반환된다(response);
            assertThat(renewalAccessTokenResponse.getAccessToken()).isNotEmpty();
        });
    }

    @DisplayName("로그아웃을 시도하면 서버내의 리프레시 토큰을 제거하고 상태코드 204를 리턴한다.")
    @Test
    void 로그아웃을_시도하면_서버내의_리프레시_토큰을_제거하고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse tokenResponse = loginResponse.as(AccessTokenResponse.class);
        final String refreshToken = loginResponse.headers().getValue("Set-Cookie");

        // when
        ExtractableResponse<Response> response = 로그아웃을_한다(tokenResponse, refreshToken);

        // then
        assertAll(() -> {
            상태코드_204이_반환된다(response);
        });
    }

    public static ExtractableResponse<Response> 로그아웃을_한다(AccessTokenResponse accessTokenResponse, String refreshToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(refreshToken)
                .when().delete("/auth/logout")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static void 상태코드_204이_반환된다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
