package moheng.acceptance;

import static moheng.fixture.MemberFixtures.스텁_이메일;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.auth.dto.AccessTokenResponse;
import moheng.auth.dto.OAuthUriResponse;
import moheng.auth.dto.TokenRequest;
import moheng.auth.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTestConfig {

    @DisplayName("카카오 소셜 로그인을 위한 Authorization URI 를 생성한다")
    @Test
    void 카카오_소셜_로그인을_위한_Authorization_URI_를_생성한다() {
        // given, when
        ExtractableResponse<Response> response = generateUri("KAKAO");
        OAuthUriResponse oAuthUriResponse = response.as(OAuthUriResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(oAuthUriResponse.getoAuthUri()).contains("https://");
        });
    }

    public static ExtractableResponse<Response> generateUri(final String oauthProvider) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/auth/{provider}/link", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static void 상태코드_200이_반환된다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
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

    public static ExtractableResponse<Response> 자체_토큰을_생성한다(final String oauthProvider, final String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(code))
                .when().post("/auth/{oAuthProvider}/login", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static void 상태코드_201이_반환된다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
