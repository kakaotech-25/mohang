package moheng.acceptance;

import static moheng.acceptance.fixture.KeywordAcceptenceFixture.키워드를_생성한다;
import static moheng.acceptance.fixture.TripKeywordAcceptenceFixture.*;
import static moheng.acceptance.fixture.TripAcceptenceFixture.*;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_200이_반환된다;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_204이_반환된다;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.trip.dto.FindTripsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        // given, when
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

        ExtractableResponse<Response> resultResponse = 상위_30개_여행지를_찾는다();

        FindTripsResponse findAllLiveInformationResponse
                = resultResponse.as(FindTripsResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(resultResponse);
            assertThat(findAllLiveInformationResponse.getFindTripResponses()).hasSize(3);
        });
    }
}
