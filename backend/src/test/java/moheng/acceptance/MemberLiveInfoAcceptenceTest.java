package moheng.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.자체_토큰을_생성한다;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_204이_반환된다;
import static moheng.acceptance.fixture.LiveInfoAcceptenceFixture.생활정보를_생성한다;
import static moheng.acceptance.fixture.MemberLiveInfoAcceptenceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberLiveInfoAcceptenceTest extends AcceptanceTestConfig {

    @DisplayName("멤버 생활정보 수정에 성공하면 상태코드 204를 리턴한다.")
    @Test
    void 멤버_생활정보_수정에_성공하면_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        생활정보를_생성한다("생활정보1");
        생활정보를_생성한다("생활정보2");

        // when
        ExtractableResponse<Response> resultResponse = 회원의_생활정보를_수정한다(accessTokenResponse.getAccessToken());

        // then
        assertAll(() -> {
            상태코드_204이_반환된다(resultResponse);
        });
    }
}
