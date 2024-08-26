package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class KeywordAcceptenceFixture {
    public static ExtractableResponse<Response> 키워드를_생성한다(String name) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new KeywordCreateRequest(name))
                .when().post("/api/keyword")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 키워드_리스트로_여행지를_추천받는다(String accessToken, TripsByKeyWordsRequest tripsByKeyWordsRequest) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tripsByKeyWordsRequest)
                .when().post("/api/keyword/trip/recommend")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 랜덤_키워드_리스트로_여행지를_추천받는다() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/keyword/random/trip")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
