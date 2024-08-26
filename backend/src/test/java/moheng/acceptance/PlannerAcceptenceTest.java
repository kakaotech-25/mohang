package moheng.acceptance;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.생활정보로_회원가입_한다;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static moheng.acceptance.fixture.TripScheduleTestFixture.플래너에_여행_일정을_생성한다;
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
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.planner.dto.FindPlannerOrderByDateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

public class PlannerAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("플래너의 여행지를 최신순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 플래너의_여행지를_날짜순으로_조회하고_상태코드_200을_라턴한다() {
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

        ExtractableResponse<Response> findOrderByDateResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().get("/api/planner/date")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        FindPlannerOrderByDateResponse resultResponse = findOrderByDateResponse.as(FindPlannerOrderByDateResponse.class);

        assertAll(() -> {
            assertThat(findOrderByDateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(resultResponse.getTripScheduleResponses()).hasSize(3);
            assertThat(resultResponse.getTripScheduleResponses().get(0).getScheduleName()).isEqualTo("여행 일정1");
            assertThat(resultResponse.getTripScheduleResponses().get(2).getScheduleName()).isEqualTo("여행 일정3");
        });
    }
}
