package moheng.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;

public class KeywordAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("키워드 기반 여행지를 추천하고 상태코드 200을 리턴한다.")
    @Test
    void 키워드_기반_여행지를_추천하고_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);
    }
}
