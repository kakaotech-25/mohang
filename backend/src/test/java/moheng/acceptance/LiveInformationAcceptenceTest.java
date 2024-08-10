package moheng.acceptance;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.*;
import static moheng.acceptance.fixture.HttpStatus.*;
import static moheng.acceptance.fixture.LiveInfoAcceptenceFixture.생활정보를_생성한다;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LiveInformationAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("생활정보를 생성하면 상태코드 204를 리턴한다.")
    @Test
    void 생활정보를_생성하면_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> response = 생활정보를_생성한다("생활정보1");

        // when, then
        assertAll(() -> {
            상태코드_204이_반환된다(response);
        });
    }
}
