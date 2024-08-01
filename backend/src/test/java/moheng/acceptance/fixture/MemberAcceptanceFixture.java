package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberAcceptanceFixture {

    public static ExtractableResponse<Response> 회원_본인의_정보를_조회한다(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/member/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
