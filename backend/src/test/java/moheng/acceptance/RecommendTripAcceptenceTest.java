package moheng.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static moheng.acceptance.fixture.RecommendTripAcceptenceFixture.선호_여행지를_선택한다;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static moheng.acceptance.fixture.TripAcceptenceFixture.여행지를_생성한다;

public class RecommendTripAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("사용자의 선호 여행지 정보를 저장하고 상태코드 204을 리턴한다.")
    @Test
    void 사용자의_선호_여행지_정보를_저장하고_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        여행지를_생성한다("롯데월드1", 1L);

        ExtractableResponse<Response> resultResponse = 선호_여행지를_선택한다(1L, accessTokenResponse.getAccessToken());
    }
}
