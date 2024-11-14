package moheng.acceptance;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.*;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.*;
import static moheng.acceptance.fixture.LiveInfoAcceptenceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.response.AccessTokenResponse;
import moheng.liveinformation.dto.response.FindAllLiveInformationResponse;
import moheng.liveinformation.dto.response.FindMemberLiveInformationResponses;
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

    @DisplayName("모든 생활정보를 찾고 상태코드 200을 리턴한다.")
    @Test
    void 모든_생활정보를_찾고_상태코드_200을_리턴한다() {
        // given
        생활정보를_생성한다("생활정보1");
        생활정보를_생성한다("생활정보2");
        생활정보를_생성한다("생활정보3");

        // when
        ExtractableResponse<Response> response = 모든_생활정보를_찾는다();

        FindAllLiveInformationResponse findAllLiveInformationResponse
                = response.as(FindAllLiveInformationResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(response);
            assertThat(findAllLiveInformationResponse.getLiveInformationResponses()).hasSize(3);
        });
    }

    @DisplayName("멤버의 생활정보를 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 멤버의_생활정보를_조회하고_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        생활정보를_생성한다("생활정보1");
        생활정보를_생성한다("생활정보2");
        생활정보를_생성한다("생활정보3");
        생활정보를_생성한다("생활정보4");
        생활정보를_생성한다("생활정보5");

        ExtractableResponse<Response> liveInfoSignUpResponse = 생활정보로_회원가입_한다(accessTokenResponse);

        // when
        ExtractableResponse<Response> resultResponse = 멤버의_생활정보를_찾는다(accessTokenResponse);
        FindMemberLiveInformationResponses memberLiveInfoResponse = resultResponse.as(FindMemberLiveInformationResponses.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(resultResponse);
            assertThat(memberLiveInfoResponse.getLiveInfoResponses()).hasSize(5);
        });
    }
}
