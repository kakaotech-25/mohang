package moheng.acceptance;


import static moheng.acceptance.fixture.HttpStatus.상태코드_200이_반환된다;
import static moheng.acceptance.fixture.TripKeywordFixture.*;
import static moheng.acceptance.fixture.RecommendTripFixture.*;
import static moheng.acceptance.fixture.TripAcceptenceFixture.*;
import static moheng.acceptance.fixture.KeywordFixture.*;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.trip.dto.FindTripsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class KeywordAcceptenceTest extends AcceptanceTestConfig {

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
}
