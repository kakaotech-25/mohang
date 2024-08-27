package moheng.acceptance;

import static moheng.acceptance.fixture.TripScheduleTestFixture.*;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
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
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.trip.dto.TripCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

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

        long tripId = 1L;
        long scheduleId = 1L;

        // when
        ExtractableResponse<Response> resultResponse =RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .when().post("/api/schedule/trip/{tripId}/{scheduleId}", tripId, scheduleId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();

        assertAll(() -> {
            assertThat(resultResponse.statusCode()).isEqualTo(204);
        });
    }
}
