package moheng.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static moheng.acceptance.fixture.TripAcceptenceFixture.여행지를_생성한다;

public class RecommendTripAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("사용자의 선호 여행지 정보를 저장하고 상태코드 204을 리턴한다.")
    @Test
    void 사용자의_선호_여행지_정보를_저장하고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        여행지를_생성한다("롯데월드1", 1L);

        ExtractableResponse<Response> resultResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .body(new RecommendTripCreateRequest(1L))
                .when().post("/recommend")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();;
    }
}
