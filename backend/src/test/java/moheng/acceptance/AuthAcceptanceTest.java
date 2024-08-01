package moheng.acceptance;

import static moheng.fixture.MemberFixtures.스텁_이메일;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.auth.dto.OAuthUriResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthAcceptanceTest extends AcceptanceTestConfig {

    @DisplayName("카카오 소셜 로그인을 위한 Authorization URI 를 생성한다")
    @Test
    void 카카오_소셜_로그인을_위한_Authorization_URI_를_생성한다() {
        // given, when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/auth/{provider}/link", "KAKAO")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        OAuthUriResponse oAuthUriResponse = response.as(OAuthUriResponse.class);

        // then
        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(oAuthUriResponse.getoAuthUri()).contains("https://");
        });
    }
}
