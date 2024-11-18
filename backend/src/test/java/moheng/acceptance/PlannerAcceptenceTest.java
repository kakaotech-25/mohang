package moheng.acceptance;

import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_200이_반환된다;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_204이_반환된다;
import static moheng.acceptance.fixture.PlannerAcceptenceFixture.*;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static moheng.acceptance.fixture.PlannerAcceptenceFixture.플래너_여행지를_이름순으로_조회한다;
import static moheng.acceptance.fixture.TripAcceptenceFixture.여행지를_생성한다;
import static moheng.acceptance.fixture.TripScheduleAcceptenceTestFixture.플래너에_여행_일정을_생성한다;
import static moheng.acceptance.fixture.TripScheduleAcceptenceTestFixture.플래너에_여행지를_담는다;
import static moheng.acceptance.fixture.PlannerAcceptenceFixture.모든_멤버의_공개된_범위내의_플래너_여행지를_날짜순으로_조회한다;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.response.AccessTokenResponse;
import moheng.planner.dto.request.*;
import moheng.planner.dto.response.*;
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
            상태코드_200이_반환된다(findOrderByDateResponse);
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
            상태코드_200이_반환된다(findOrderByDateResponse);
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
            상태코드_200이_반환된다(findOrderByDateResponse);
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
            상태코드_204이_반환된다(resultResponse);
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
            상태코드_204이_반환된다(resultResponse);
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
            상태코드_204이_반환된다(resultResponse);
            assertThat(sizeResponse.getTripScheduleResponses().size()).isEqualTo(0);
        });
    }

    @DisplayName("주어진 범위에 해당하는 플래너 여행 일정들을 내림차순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 주어진_범위에_해당하는_플래너_여행_일정들을_내림차순으로_조회하고_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        여행지를_생성한다("여행지1", 1L);
        여행지를_생성한다("여행지2", 2L);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("가 일정",
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(2000, 9, 10)
                ));

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("나 일정",
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));

        플래너에_여행지를_담는다(accessTokenResponse, 1L, new AddTripOnScheduleRequests(List.of(1L)));
        플래너에_여행지를_담는다(accessTokenResponse, 2L, new AddTripOnScheduleRequests(List.of(2L)));

        LocalDate 시작날짜 = LocalDate.now().minusDays(1);
        LocalDate 종료날짜 = LocalDate.now().plusDays(1);

        // when
        ExtractableResponse<Response> resultHttpResponse = 멤버의_범위내의_플래너_여행지를_날짜순으로_조회한다(accessTokenResponse, new FindPlannerOrderByDateBetweenRequest(시작날짜, 종료날짜));
        FindPlannerOrderByDateBetweenResponse findPlannerOrderByDateBetweenResponse = resultHttpResponse.as(FindPlannerOrderByDateBetweenResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(resultHttpResponse);
            assertThat(findPlannerOrderByDateBetweenResponse.getTripScheduleResponses().size()).isEqualTo(2);
            assertThat(findPlannerOrderByDateBetweenResponse.getTripScheduleResponses().get(0).getScheduleName()).isEqualTo("나 일정");
            assertThat(findPlannerOrderByDateBetweenResponse.getTripScheduleResponses().get(1).getScheduleName()).isEqualTo("가 일정");
        });
    }

    @DisplayName("모든 멤버에 대한 공개된 여행 일정 리스트를 생성날짜를 기준으로 찾고 상태코드 200을 리턴한다.")
    @Test
    void 모든_멤버에_대한_공개된_여행_일정_리스트를_생성날짜를_기준으로_찾고_상태코두_200을_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("가 일정",
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(2000, 9, 10)
                ));

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("나 일정",
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));

        LocalDate 시작날짜 = LocalDate.now().minusDays(10);
        LocalDate 종료날짜 = LocalDate.now().plusDays(10);

        // when
        ExtractableResponse<Response> resultHttpResponse = 모든_멤버의_공개된_범위내의_플래너_여행지를_날짜순으로_조회한다(accessTokenResponse, new FindPublicSchedulesForRangeRequest(시작날짜, 종료날짜));
        FindPlannerPublicForCreatedAtRangeResponses findPlannerPublicForCreatedAtRangeResponses = resultHttpResponse.as(FindPlannerPublicForCreatedAtRangeResponses.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(resultHttpResponse);
            assertThat(findPlannerPublicForCreatedAtRangeResponses.getTripScheduleResponses().size()).isEqualTo(2);
        });
    }

    @DisplayName("모든 멤버에 대한 공개 여행지 중에 검색명에 해당하는 일정을 찾고 상태코드 200을 리턴한다.")
    @Test
    void 모든_멤버에_대한_공개_여행지_중에_검색명에_해당하는_일정을_찾고_상태코드_200을_리턴한다() throws Exception {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("가 일정",
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(2000, 9, 10)
                ));

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("나 일정",
                        LocalDate.of(2000, 1, 1),
                        LocalDate.of(2030, 9, 10)
                ));

        FindSchedulesByNameRequest 검색명_요청 = new FindSchedulesByNameRequest("가 일정");

        // when
        ExtractableResponse<Response> resultHttpResponse = 모든_멤버의_공개된_플래너_여행지중_검색명을_포함하는_일정을_조회한다(accessTokenResponse, 검색명_요청);
        FindSchedulesNameResponses findSchedulesNameResponses = resultHttpResponse.as(FindSchedulesNameResponses.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(resultHttpResponse);
            assertThat(findSchedulesNameResponses.getTripScheduleResponses().size()).isEqualTo(2);
        });
    }
}
