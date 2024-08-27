package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.auth.dto.AccessTokenResponse;
import moheng.planner.dto.CreateTripScheduleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TripScheduleAcceptenceTestFixture {
    public static ExtractableResponse<Response> 플래너에_여행_일정을_생성한다(final AccessTokenResponse accessTokenResponse, final CreateTripScheduleRequest createTripScheduleRequest) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createTripScheduleRequest)
                .when().post("/api/schedule")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 플래너에_여행지를_담는다(final AccessTokenResponse accessTokenResponse, final long tripId, final long scheduleId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().post("/api/schedule/trip/{tripId}/{scheduleId}", tripId, scheduleId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 세부_일정을_찾는다(final long scheduleId, final AccessTokenResponse accessTokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().get("/api/schedule/trips/{scheduleId}", scheduleId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
