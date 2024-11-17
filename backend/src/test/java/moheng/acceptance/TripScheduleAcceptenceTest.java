package moheng.acceptance;

import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_200이_반환된다;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_204이_반환된다;
import static moheng.acceptance.fixture.TripScheduleAcceptenceTestFixture.*;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static moheng.acceptance.fixture.TripScheduleAcceptenceTestFixture.세부_일정을_찾는다;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static moheng.acceptance.fixture.TripAcceptenceFixture.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.response.AccessTokenResponse;
import moheng.planner.dto.request.AddTripOnScheduleRequests;
import moheng.planner.dto.request.CreateTripScheduleRequest;
import moheng.planner.dto.response.FindTripsOnSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class TripScheduleAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("플래너에 여행 일정을 생성하고 상태코드 204를 리턴한다.")
    @Test
    void 플래너에_여행_일정을_생성하고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        // when
        ExtractableResponse<Response> createScheduleResponse = 플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정1",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 9, 10)
                ));

        //  then
        assertAll(() -> {
            상태코드_204이_반환된다(createScheduleResponse);
            assertThat(createScheduleResponse.statusCode()).isEqualTo(204);
        });
    }

    @DisplayName("현재 여행지를 플래너 일정에 담고 상태코드 204를 리턴한다.")
    @Test
    void 현재_여행지를_플래너_일정에_담고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정1",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 9, 10))
        );
        여행지를_생성한다("여행지", 1L);

        // when
        ExtractableResponse<Response> resultResponse = 플래너에_여행지를_담는다(accessTokenResponse, 1L, new AddTripOnScheduleRequests(List.of(1L)));

        assertAll(() -> {
            상태코드_204이_반환된다(resultResponse);
        });
    }

    @DisplayName("세부 일정 정보를 여행지 리스트와 함께 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 세부_일정_정보를_여행지_리스트와_함께_조회하고_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정1",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 9, 10))
        );
        여행지를_생성한다("여행지1", 1L);
        여행지를_생성한다("여행지2", 2L);
        여행지를_생성한다("여행지3", 3L);

        플래너에_여행지를_담는다(accessTokenResponse, 1L, new AddTripOnScheduleRequests(List.of(1L)));
        플래너에_여행지를_담는다(accessTokenResponse, 2L, new AddTripOnScheduleRequests(List.of(1L)));
        플래너에_여행지를_담는다(accessTokenResponse, 3L, new AddTripOnScheduleRequests(List.of(1L)));

        // when
        ExtractableResponse<Response> resultResponse = 세부_일정을_찾는다(1L, accessTokenResponse);
        FindTripsOnSchedule findTripsOnSchedule = resultResponse.as(FindTripsOnSchedule.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(resultResponse);
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules()).hasSize(3);
        });
    }

    @DisplayName("세부 일정내 여행지 리스트 정렬 순서를 변경하고 상태코드 204을 리턴한다.")
    @Test
    void 세부_일정내_여행지_리스트_정렬_순서를_변경하고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정1",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 9, 10))
        );
        여행지를_생성한다("여행지1", 1L);
        여행지를_생성한다("여행지2", 2L);
        여행지를_생성한다("여행지3", 3L);

        플래너에_여행지를_담는다(accessTokenResponse, 1L, new AddTripOnScheduleRequests(List.of(1L)));
        플래너에_여행지를_담는다(accessTokenResponse, 2L, new AddTripOnScheduleRequests(List.of(1L)));
        플래너에_여행지를_담는다(accessTokenResponse, 3L, new AddTripOnScheduleRequests(List.of(1L)));

        // when
        ExtractableResponse<Response> resultResponse = 세부_일정내_여행지_정렬_순서를_수정한다(1L, accessTokenResponse);

        FindTripsOnSchedule findTripsOnSchedule = 세부_일정을_찾는다(1L, accessTokenResponse).as(FindTripsOnSchedule.class);

        // then
        assertAll(() -> {
            상태코드_204이_반환된다(resultResponse);
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules()).hasSize(3);
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules().get(0).getPlaceName()).isEqualTo("여행지3");
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules().get(1).getPlaceName()).isEqualTo("여행지1");
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules().get(2).getPlaceName()).isEqualTo("여행지2");
        });
    }

    @DisplayName("세부 일정내 여행지를 제거하고 상태코드 204를 리턴한다.")
    @Test
    void 세부_일정내_여행지를_제거하고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        플래너에_여행_일정을_생성한다(
                accessTokenResponse,
                new CreateTripScheduleRequest("여행 일정1",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2022, 9, 10))
        );
        여행지를_생성한다("여행지1", 1L);
        여행지를_생성한다("여행지2", 2L);
        여행지를_생성한다("여행지3", 3L);

        플래너에_여행지를_담는다(accessTokenResponse, 1L, new AddTripOnScheduleRequests(List.of(1L)));
        플래너에_여행지를_담는다(accessTokenResponse, 2L, new AddTripOnScheduleRequests(List.of(1L)));
        플래너에_여행지를_담는다(accessTokenResponse, 3L, new AddTripOnScheduleRequests(List.of(1L)));

        // when
        long scheduleId = 1L;
        long tripId = 1L;

        ExtractableResponse<Response> resultResponse = 세부_일정내_특정_여행지를_제거한다(scheduleId, tripId, accessTokenResponse);

        FindTripsOnSchedule findTripsOnSchedule = 세부_일정을_찾는다(1L, accessTokenResponse).as(FindTripsOnSchedule.class);

        // then
        assertAll(() -> {
            상태코드_204이_반환된다(resultResponse);
            assertThat(findTripsOnSchedule.getFindTripsOnSchedules()).hasSize(2);
        });
    }
}
