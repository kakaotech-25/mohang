package moheng.acceptance;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.*;
import static moheng.acceptance.fixture.HttpStatus.*;
import static moheng.acceptance.fixture.LiveInfoAcceptenceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.liveinformation.dto.FindAllLiveInformationResponse;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
}
