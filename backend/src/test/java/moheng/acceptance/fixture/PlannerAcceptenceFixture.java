package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.auth.dto.response.AccessTokenResponse;
import moheng.planner.dto.request.FindPlannerOrderByDateBetweenRequest;
import moheng.planner.dto.request.UpdateTripScheduleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PlannerAcceptenceFixture {
    public static ExtractableResponse<Response> 플래너_여행지를_날짜순으로_조회한다(final AccessTokenResponse accessTokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().get("/api/planner/date")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 플래너_여행지를_최신순으로_조회한다(final AccessTokenResponse accessTokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().get("/api/planner/recent")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 플래너_여행지를_이름순으로_조회한다(final AccessTokenResponse accessTokenResponse) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().get("/api/planner/name")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 여행_일정을_수정한다(final AccessTokenResponse accessTokenResponse, final UpdateTripScheduleRequest updateTripScheduleRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateTripScheduleRequest)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().put("/api/planner/schedule")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 여행_일정을_삭제한다(final AccessTokenResponse accessTokenResponse, final long scheduleId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().delete("/api/planner/schedule/{scheduleId}", scheduleId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 멤버의_범위내의_플래너_여행지를_날짜순으로_조회한다(final AccessTokenResponse accessTokenResponse, final FindPlannerOrderByDateBetweenRequest findPlannerOrderByDateBetweenRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(findPlannerOrderByDateBetweenRequest)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().get("/api/planner/range")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
