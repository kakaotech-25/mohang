package moheng.acceptance;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.생활정보로_회원가입_한다;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static moheng.acceptance.fixture.TripAcceptenceFixture.*;
import static moheng.acceptance.fixture.LiveInfoAcceptenceFixture.*;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.auth.dto.TokenRequest;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.dto.FindTripResponse;
import moheng.trip.dto.FindTripsResponse;
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
        여행지를_생성한다("롯데월드4", 4L); 여행지를_생성한다("롯데월드5", 5L); 여행지를_생성한다("롯데월드6", 6L);
        여행지를_생성한다("롯데월드7", 7L); 여행지를_생성한다("롯데월드8", 8L); 여행지를_생성한다("롯데월드9", 9L);
        여행지를_생성한다("롯데월드10", 10L); 여행지를_생성한다("롯데월드11", 11L); 여행지를_생성한다("롯데월드12", 12L);
        키워드를_생성한다("키워드1"); 키워드를_생성한다("키워드2"); 키워드를_생성한다("키워드3");
        여행지_키워드를_생성한다(1L, 1L); 여행지_키워드를_생성한다(2L, 2L);
        여행지_키워드를_생성한다(3L, 3L); 여행지_키워드를_생성한다(2L, 3L);
        여행지_키워드를_생성한다(2L, 4L); 여행지_키워드를_생성한다(3L, 5L);
        여행지_키워드를_생성한다(2L, 6L); 여행지_키워드를_생성한다(3L, 7L);
        여행지_키워드를_생성한다(2L, 8L); 여행지_키워드를_생성한다(3L, 8L);
        여행지_키워드를_생성한다(3L, 9L); 여행지_키워드를_생성한다(2L, 10L);

        선호_여행지를_선택한다(1L, accessTokenResponse.getAccessToken()); 선호_여행지를_선택한다(2L, accessTokenResponse.getAccessToken());
        선호_여행지를_선택한다(3L, accessTokenResponse.getAccessToken()); 선호_여행지를_선택한다(4L, accessTokenResponse.getAccessToken());
        선호_여행지를_선택한다(5L, accessTokenResponse.getAccessToken());

        멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 1L); 멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 2L);
        멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 3L); 멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 4L);
        멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 5L); 멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 6L);
        멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 7L); 멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 8L);
        멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 9L); 멤버_여행지를_생성한다(accessTokenResponse.getAccessToken(), 10L);

        생활정보를_생성한다("생활정보1"); 생활정보를_생성한다("생활정보2");
        생활정보로_회원가입_한다(accessTokenResponse);
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 1L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 2L, 1L);
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 3L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 4L, 1L);
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 5L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 6L, 1L);
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 7L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 8L, 1L);
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 9L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 10L, 1L);

        // when, then
        ExtractableResponse<Response> resultResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().get("/recommend")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        FindTripsResponse findTripsResponse = resultResponse.as(FindTripsResponse.class);

        assertAll(() -> {
            assertThat(resultResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(findTripsResponse.getFindTripResponses()).hasSize(10);
        });
    }
}
