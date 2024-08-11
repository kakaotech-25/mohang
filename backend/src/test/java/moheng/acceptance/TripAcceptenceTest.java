package moheng.acceptance;

import static moheng.acceptance.fixture.TripAcceptenceFixture.*;
import static moheng.acceptance.fixture.HttpStatus.상태코드_200이_반환된다;
import static moheng.acceptance.fixture.HttpStatus.상태코드_204이_반환된다;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.member.dto.request.SignUpInterestTripsRequest;
import moheng.trip.dto.TripCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

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
}
