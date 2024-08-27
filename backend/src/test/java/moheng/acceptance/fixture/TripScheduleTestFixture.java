package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.auth.dto.AccessTokenResponse;
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.trip.dto.TripKeywordCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

public class TripScheduleTestFixture {
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
}
