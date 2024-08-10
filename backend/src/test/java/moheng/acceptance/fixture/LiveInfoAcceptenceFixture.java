package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import org.springframework.http.MediaType;

public class LiveInfoAcceptenceFixture {
    public static ExtractableResponse<Response> 생활정보를_생성한다(String name) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LiveInformationCreateRequest(name))
                .when().post("/live/info")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }
}