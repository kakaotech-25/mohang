package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.trip.dto.request.TripCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TripAcceptenceFixture {
    public static ExtractableResponse<Response> 여행지를_생성한다(final String name, final long contentId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TripCreateRequest(name, "서울특별시 송파구", contentId, "롯데월드 관련 설명", "https://lotte-world.png"))
                .when().post("/api/trip")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 상위_30개_여행지를_찾는다() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/trip/find/interested")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 멤버_여행지를_생성한다(final String accessToken, final long tripId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().post("/api/trip/member/{tripId}", tripId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 여행지의_생활정보를_생성한다(final String accessToken, final long tripId, final long liveInfoId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().post("/api/live/info/trip/{tripId}/{liveInfoId}", tripId, liveInfoId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
