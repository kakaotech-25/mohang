package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class RecommendTripAcceptenceFixture {
    public static ExtractableResponse<Response> 선호_여행지를_선택한다(final long tripId, final String accessToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(new RecommendTripCreateRequest(tripId))
                .when().post("/api/recommend")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> AI_맞춤_추천_여행지를_조회한다(final String accessToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/api/recommend")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
