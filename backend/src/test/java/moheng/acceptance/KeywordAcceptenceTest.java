package moheng.acceptance;


import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_200이_반환된다;
import static moheng.acceptance.fixture.TripKeywordAcceptenceFixture.*;
import static moheng.acceptance.fixture.TripAcceptenceFixture.*;
import static moheng.acceptance.fixture.KeywordAcceptenceFixture.*;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.keyword.dto.FindAllKeywordResponses;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.trip.dto.FindTripsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

public class KeywordAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("모든 키워드를 찾고 상태코드 200을 리턴한다.")
    @Test
    void 모든_키워드를_찾고_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);
        String accessToken = accessTokenResponse.getAccessToken();

        키워드를_생성한다("키워드1");
        키워드를_생성한다("키워드2");
        키워드를_생성한다("키워드3");

        // when
        ExtractableResponse<Response> findAllKeywordResponse = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/keyword")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        FindAllKeywordResponses findAllKeywordResponses = findAllKeywordResponse.as(FindAllKeywordResponses.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(findAllKeywordResponse);
            assertThat(findAllKeywordResponses.getFindAllKeywordResponses().size()).isEqualTo(3L);
        });
    }

    @DisplayName("키워드 기반 여행지를 추천하고 상태코드 200을 리턴한다.")
    @Test
    void 키워드_기반_여행지를_추천하고_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);
        String accessToken = accessTokenResponse.getAccessToken();

        키워드를_생성한다("키워드1");
        키워드를_생성한다("키워드2");
        키워드를_생성한다("키워드3");
        여행지를_생성한다("여행지1", 1L);
        여행지를_생성한다("여행지2", 2L);
        여행지를_생성한다("여행지3", 3L);

        여행지_키워드를_생성한다(1L, 1L);
        여행지_키워드를_생성한다(2L, 2L);
        여행지_키워드를_생성한다(3L, 3L);

        // when
        ExtractableResponse<Response> recommendResponse = 키워드_리스트로_여행지를_추천받는다(
                accessToken, new TripsByKeyWordsRequest(List.of(1L, 2L, 3L))
        );
        FindTripsResponse responseResult = recommendResponse.as(FindTripsResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(recommendResponse);
            assertThat(responseResult.getFindTripResponses().size()).isEqualTo(3L);
        });
    }

    @DisplayName("무작위 랜덤 키워드 기반 여행지를 추천하고 상태코드 200을 리턴한다.")
    @Test
    void 무작위_랜덤_키워드_기반_여행지를_추천하고_상태코드_200을_리턴한다() {
        // given
        키워드를_생성한다("키워드1");
        여행지를_생성한다("여행지1", 1L);
        여행지를_생성한다("여행지2", 2L);
        여행지를_생성한다("여행지3", 3L);

        여행지_키워드를_생성한다(1L, 1L);
        여행지_키워드를_생성한다(1L, 2L);
        여행지_키워드를_생성한다(1L, 3L);

        // when
        ExtractableResponse<Response> recommendResponse = 랜덤_키워드_리스트로_여행지를_추천받는다();
        FindTripsResponse responseResult = recommendResponse.as(FindTripsResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(recommendResponse);
            assertThat(responseResult.getFindTripResponses().size()).isEqualTo(3L);
        });
    }
}
