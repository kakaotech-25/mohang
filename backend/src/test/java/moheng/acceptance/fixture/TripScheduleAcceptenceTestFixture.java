package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.auth.dto.response.AccessTokenResponse;
import moheng.planner.dto.request.AddTripOnScheduleRequests;
import moheng.planner.dto.request.CreateTripScheduleRequest;
import moheng.planner.dto.request.UpdateTripOrdersRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

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

    public static ExtractableResponse<Response> 플래너에_여행지를_담는다(final AccessTokenResponse accessTokenResponse, final long tripId, final AddTripOnScheduleRequests addTripOnScheduleRequests) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .body(addTripOnScheduleRequests)
                .when().post("/api/schedule/trip/{tripId}", tripId)
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

    public static ExtractableResponse<Response> 세부_일정내_여행지_정렬_순서를_수정한다(final long scheduleId, final AccessTokenResponse accessTokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .body(new UpdateTripOrdersRequest(List.of(3L, 1L, 2L)))
                .when().post("/api/schedule/trips/orders/{scheduleId}", scheduleId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 세부_일정내_특정_여행지를_제거한다(final long scheduleId, final long tripId, final AccessTokenResponse accessTokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().delete("/api/schedule/{scheduleId}/{tripId}", scheduleId, tripId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
