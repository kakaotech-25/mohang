package moheng.acceptance;

import static moheng.acceptance.fixture.TripAcceptenceFixture.*;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.auth.dto.TokenRequest;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static moheng.acceptance.fixture.KeywordAcceptenceFixture.키워드를_생성한다;
import static moheng.acceptance.fixture.RecommendTripAcceptenceFixture.선호_여행지를_선택한다;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static moheng.acceptance.fixture.TripAcceptenceFixture.여행지를_생성한다;
import static moheng.acceptance.fixture.TripKeywordAcceptenceFixture.여행지_키워드를_생성한다;

public class RecommendTripAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("사용자의 선호 여행지 정보를 저장하고 상태코드 204을 리턴한다.")
    @Test
    void 사용자의_선호_여행지_정보를_저장하고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        여행지를_생성한다("롯데월드1", 1L);

        ExtractableResponse<Response> resultResponse = 선호_여행지를_선택한다(1L, accessTokenResponse.getAccessToken());
    }

    @DisplayName("AI 맞춤 추천 여행지를 조회하고 상태코드 200을 리턴한다.")
    @Test
    void AI_맞춤_추천_여행지를_조회하고_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        여행지를_생성한다("롯데월드1", 1L); 여행지를_생성한다("롯데월드2", 2L); 여행지를_생성한다("롯데월드3", 3L);
        여행지를_생성한다("롯데월드3", 4L); 여행지를_생성한다("롯데월드3", 5L);
        키워드를_생성한다("키워드1"); 키워드를_생성한다("키워드2"); 키워드를_생성한다("키워드3");
        여행지_키워드를_생성한다(1L, 1L); 여행지_키워드를_생성한다(2L, 2L);
        여행지_키워드를_생성한다(3L, 3L); 여행지_키워드를_생성한다(2L, 3L);
        여행지_키워드를_생성한다(2L, 4L); 여행지_키워드를_생성한다(2L, 5L);

        선호_여행지를_선택한다(1L, accessTokenResponse.getAccessToken()); 선호_여행지를_선택한다(2L, accessTokenResponse.getAccessToken());
        선호_여행지를_선택한다(3L, accessTokenResponse.getAccessToken()); 선호_여행지를_선택한다(4L, accessTokenResponse.getAccessToken());
        선호_여행지를_선택한다(5L, accessTokenResponse.getAccessToken());

        멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 1L); 멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 2L);
        멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 3L); 멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 4L);
        멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 5L);

        // when, then
        ExtractableResponse<Response> resultResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().get("/recommend")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();


    }
}
