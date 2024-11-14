package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.auth.dto.response.AccessTokenResponse;
import moheng.liveinformation.dto.request.LiveInformationCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class LiveInfoAcceptenceFixture {
    public static ExtractableResponse<Response> 생활정보를_생성한다(final String name) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LiveInformationCreateRequest(name))
                .when().post("/api/live/info")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 모든_생활정보를_찾는다() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/live/info/all")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 멤버의_생활정보를_찾는다(final AccessTokenResponse accessTokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().get("/api/live/info/member")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
