package moheng.acceptance;

import static moheng.acceptance.fixture.PlannerAcceptenceFixture.*;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static moheng.acceptance.fixture.PlannerAcceptenceFixture.플래너_여행지를_이름순으로_조회한다;
import static moheng.acceptance.fixture.TripAcceptenceFixture.여행지를_생성한다;
import static moheng.acceptance.fixture.TripScheduleAcceptenceTestFixture.플래너에_여행_일정을_생성한다;
import static moheng.acceptance.fixture.TripScheduleAcceptenceTestFixture.플래너에_여행지를_담는다;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.planner.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

public class PlannerAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("플래너의 일정을 날짜순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 플래너의_일정을_날짜순으로_조회하고_상태코드_200을_라턴한다() {
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정1",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 9, 10)
                ));
        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정2",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 9, 10)
                ));
        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정3",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 9, 10)
                ));

        ExtractableResponse<Response> findOrderByDateResponse = 플래너_여행지를_날짜순으로_조회한다(accessTokenResponse);
        FindPlannerOrderByDateResponse resultResponse = findOrderByDateResponse.as(FindPlannerOrderByDateResponse.class);

        assertAll(() -> {
            assertThat(findOrderByDateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(resultResponse.getTripScheduleResponses()).hasSize(3);
            assertThat(resultResponse.getTripScheduleResponses().get(0).getScheduleName()).isEqualTo("여행 일정1");
            assertThat(resultResponse.getTripScheduleResponses().get(2).getScheduleName()).isEqualTo("여행 일정3");
        });
    }

    @DisplayName("플래너의 일정을 최신순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 플래너의_일정을_최신순으로_조회하고_상태코드_200을_라턴한다() {
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정1",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));
        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정2",
                        LocalDate.of(2023, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));
        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정3",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));

        ExtractableResponse<Response> findOrderByDateResponse = 플래너_여행지를_최신순으로_조회한다(accessTokenResponse);
        FindPlannerOrderByRecentResponse resultResponse = findOrderByDateResponse.as(FindPlannerOrderByRecentResponse.class);

        assertAll(() -> {
            assertThat(findOrderByDateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(resultResponse.getTripScheduleResponses()).hasSize(3);
            assertThat(resultResponse.getTripScheduleResponses().get(0).getScheduleName()).isEqualTo("여행 일정3");
            assertThat(resultResponse.getTripScheduleResponses().get(2).getScheduleName()).isEqualTo("여행 일정1");
        });
    }

    @DisplayName("플래너의 일정을 이름순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 플래너의_일정을_이름순으로_조회하고_상태코드_200을_라턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("가 일정",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));
        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("다 일정",
                        LocalDate.of(2023, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));
        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("나 일정",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));

        // when
        ExtractableResponse<Response> findOrderByDateResponse = 플래너_여행지를_이름순으로_조회한다(accessTokenResponse);
        FindPLannerOrderByNameResponse resultResponse = findOrderByDateResponse.as(FindPLannerOrderByNameResponse.class);

        // then
        assertAll(() -> {
            assertThat(findOrderByDateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(resultResponse.getTripScheduleResponses()).hasSize(3);
            assertThat(resultResponse.getTripScheduleResponses().get(0).getScheduleName()).isEqualTo("가 일정");
            assertThat(resultResponse.getTripScheduleResponses().get(2).getScheduleName()).isEqualTo("다 일정");
        });
    }

    @DisplayName("여행 일정을 수정하고 상태코드 204를 리턴한다.")
    @Test
    void 여행_일정을_수정히고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("가 일정",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));

        // when
        ExtractableResponse<Response> resultResponse = 여행_일정을_수정한다(accessTokenResponse,
                new UpdateTripScheduleRequest(1L, "새로운 일정명",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2030, 9, 10)));

        // then
        assertAll(() -> {
            assertThat(resultResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }

    @DisplayName("여행 일정을 삭제하고 상태코드 204를 리턴한다.")
    @Test
    void 여행_일정을_삭제하고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("가 일정",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));

        // when
        ExtractableResponse<Response> resultResponse = 여행_일정을_삭제한다(accessTokenResponse, 1L);

        // then
        assertAll(() -> {
            assertThat(resultResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }

    @DisplayName("여행지를 삭제하면 그 안의 여행지들도 함께 삭제되고 상태코드 204를 리턴한다.")
    @Test
    void 여행지를_삭제하면_그_안의_여행지들도_함께_삭제되고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        여행지를_생성한다("여행지1", 1L);
        여행지를_생성한다("여행지2", 2L);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("가 일정",
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));

        플래너에_여행지를_담는다(accessTokenResponse, 1L, new AddTripOnScheduleRequests(List.of(1L)));
        플래너에_여행지를_담는다(accessTokenResponse, 2L, new AddTripOnScheduleRequests(List.of(1L)));

        // when
        ExtractableResponse<Response> resultResponse = 여행_일정을_삭제한다(accessTokenResponse, 1L);

        ExtractableResponse<Response> findPlannerTripsResponse = 플래너_여행지를_이름순으로_조회한다(accessTokenResponse);
        FindPLannerOrderByNameResponse sizeResponse = findPlannerTripsResponse.as(FindPLannerOrderByNameResponse.class);

        // then
        assertAll(() -> {
            assertThat(resultResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            assertThat(sizeResponse.getTripScheduleResponses().size()).isEqualTo(0);
        });
    }
}
