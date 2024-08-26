package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.trip.dto.TripKeywordCreateRequest;
import org.springframework.http.MediaType;

public class TripKeywordAcceptenceFixture {
    public static ExtractableResponse<Response> 여행지_키워드를_생성한다(final long keywordId, final long tripId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TripKeywordCreateRequest(tripId, keywordId))
                .when().post("/api/keyword/trip")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
