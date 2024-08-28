package moheng.acceptance;

import static moheng.acceptance.fixture.RecommendTripAcceptenceFixture.*;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static moheng.acceptance.fixture.KeywordAcceptenceFixture.키워드를_생성한다;
import static moheng.acceptance.fixture.LiveInfoAcceptenceFixture.*;
import static moheng.acceptance.fixture.TripKeywordAcceptenceFixture.*;
import static moheng.acceptance.fixture.TripAcceptenceFixture.*;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_200이_반환된다;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_204이_반환된다;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.trip.dto.FindTripWithSimilarTripsResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripKeywordCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TripAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("여행지를 생성하면 상태코드 204를 리턴한다.")
    @Test
    void 여행지를_생성하면_상태코드_204를_리턴한다() {
        // given, when
        ExtractableResponse<Response> resultResponse = 여행지를_생성한다("롯데월드", 1L);

        // then
        assertAll(() -> {
            상태코드_204이_반환된다(resultResponse);
        });
    }

    @DisplayName("상위 여행지들을 방문 수 기준으로 찾고 상태코드 200을 리턴한다.")
    @Test
    void 상위_여행지들을_방문_수_기준으로_조회하고_상태코드_200을_리턴한다() {
        // given
        여행지를_생성한다("롯데월드1", 1L);
        여행지를_생성한다("롯데월드2", 2L);
        여행지를_생성한다("롯데월드3", 3L);
        키워드를_생성한다("키워드1");
        키워드를_생성한다("키워드2");
        키워드를_생성한다("키워드3");
        여행지_키워드를_생성한다(1L, 1L);
        여행지_키워드를_생성한다(2L, 2L);
        여행지_키워드를_생성한다(3L, 3L);
        여행지_키워드를_생성한다(2L, 3L);

        // when
        ExtractableResponse<Response> resultResponse = 상위_30개_여행지를_찾는다();

        FindTripsResponse findAllLiveInformationResponse
                = resultResponse.as(FindTripsResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(resultResponse);
            assertThat(findAllLiveInformationResponse.getFindTripResponses()).hasSize(3);
        });
    }

    @DisplayName("현재 여행지를 비슷한 여행지 10개와 함께 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 현재_여행지를_비슷한_여행지와_함께_조회하고_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        여행지를_생성한다("롯데월드1", 1L); 여행지를_생성한다("롯데월드2", 2L); 여행지를_생성한다("롯데월드3", 3L);
        여행지를_생성한다("롯데월드4", 4L); 여행지를_생성한다("롯데월드4", 5L); 여행지를_생성한다("롯데월드6", 6L);
        여행지를_생성한다("롯데월드7", 7L); 여행지를_생성한다("롯데월드8", 8L); 여행지를_생성한다("롯데월드9", 9L);
        여행지를_생성한다("롯데월드10", 10L);

        키워드를_생성한다("키워드1"); 키워드를_생성한다("키워드2"); 키워드를_생성한다("키워드3");

        여행지_키워드를_생성한다(1L, 1L); 여행지_키워드를_생성한다(2L, 2L);
        여행지_키워드를_생성한다(2L, 3L); 여행지_키워드를_생성한다(3L, 3L);
        여행지_키워드를_생성한다(3L, 4L); 여행지_키워드를_생성한다(3L, 5L);
        여행지_키워드를_생성한다(3L, 6L); 여행지_키워드를_생성한다(3L, 7L);
        여행지_키워드를_생성한다(3L, 8L); 여행지_키워드를_생성한다(3L, 9L);
        여행지_키워드를_생성한다(3L, 10L); 여행지_키워드를_생성한다(2L, 10L);

        생활정보를_생성한다("생활정보1");
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 1L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 2L, 1L);
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 3L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 4L, 1L);
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 5L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 6L, 1L);
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 7L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 8L, 1L);
        여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 9L, 1L); 여행지의_생활정보를_생성한다(accessTokenResponse.getAccessToken(), 10L, 1L);

        선호_여행지를_선택한다(1L, accessTokenResponse.getAccessToken());
        선호_여행지를_선택한다(2L, accessTokenResponse.getAccessToken());
        선호_여행지를_선택한다(3L, accessTokenResponse.getAccessToken());
        선호_여행지를_선택한다(4L, accessTokenResponse.getAccessToken());
        선호_여행지를_선택한다(5L, accessTokenResponse.getAccessToken());

        // when
        ExtractableResponse<Response> resultResponse = RestAssured.given().log().all()
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/trip/find/{tripId}", 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        FindTripWithSimilarTripsResponse findTripWithSimilarTripsResponse = resultResponse.as(FindTripWithSimilarTripsResponse.class);

        // then
        assertAll(() -> {
            assertThat(findTripWithSimilarTripsResponse.getSimilarTripResponses().getFindTripResponses()).hasSize(10);
            assertThat(findTripWithSimilarTripsResponse.getFindTripResponse()).isNotNull();
            assertThat(resultResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        });
    }
}
