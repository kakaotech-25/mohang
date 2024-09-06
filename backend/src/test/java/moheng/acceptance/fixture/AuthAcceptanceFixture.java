package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.auth.dto.AccessTokenResponse;
import moheng.auth.dto.TokenRequest;
import moheng.member.dto.request.SignUpInterestTripsRequest;
import moheng.member.dto.request.SignUpLiveInfoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceFixture {
    public static ExtractableResponse<Response> OAuth_인증_URI를_생성한다(final String oauthProvider) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/auth/{provider}/link", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 자체_토큰을_생성한다(final String oauthProvider, final String code) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(code))
                .when().post("/api/auth/{oAuthProvider}/login", oauthProvider)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    public static ExtractableResponse<Response> 리프레시_토큰을_통해_새로운_엑세스_토큰을_재발급_한다(final String refreshToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(refreshToken)
                .when().post("/api/auth/extend/login")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그아웃을_한다(final AccessTokenResponse accessTokenResponse, final String refreshToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(refreshToken)
                .when().delete("/api/auth/logout")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 생활정보로_회원가입_한다(final AccessTokenResponse accessTokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .body(new SignUpLiveInfoRequest(List.of("생활정보1", "생활정보2")))
                .when().post("/api/member/signup/liveinfo")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 관심_여행지로_회원가입_한다(final AccessTokenResponse accessTokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .body(new SignUpInterestTripsRequest(List.of(1L, 2L, 3L, 4L, 5L)))
                .when().post("/api/member/signup/trip")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
