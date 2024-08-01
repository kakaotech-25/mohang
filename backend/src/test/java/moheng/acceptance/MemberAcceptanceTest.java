package moheng.acceptance;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.*;
import static moheng.acceptance.fixture.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.auth.dto.TokenResponse;
import moheng.member.dto.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberAcceptanceTest extends AcceptanceTestConfig {

    @DisplayName("등록된 회원이 마이페이지를 조회하면 회원 본인의 정보와 상태코드 200을 리턴한다.")
    @Test
    void 등록된_회원이_마이페이지를_조회하면_회원_본인의_정보와_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        // when
        ExtractableResponse<Response> memberResponse = RestAssured.given().log().all()
                .auth().oauth2(accessTokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/member/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        MemberResponse responseResult = memberResponse.as(MemberResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(memberResponse);
            assertThat(responseResult.getId()).isEqualTo(1L);
        });
    }
}
