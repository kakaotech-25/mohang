package moheng.acceptance;

import static moheng.acceptance.fixture.AuthAcceptanceFixture.*;
import static moheng.acceptance.fixture.HttpStatus.상태코드_200이_반환된다;
import static moheng.acceptance.fixture.MemberAcceptanceFixture.*;
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
import moheng.member.domain.GenderType;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.member.dto.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

public class MemberAcceptanceTest extends AcceptanceTestConfig {

    @DisplayName("등록된 회원이 마이페이지를 조회하면 회원 본인의 정보와 상태코드 200을 리턴한다.")
    @Test
    void 등록된_회원이_마이페이지를_조회하면_회원_본인의_정보와_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> loginResponse = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = loginResponse.as(AccessTokenResponse.class);

        // when
        ExtractableResponse<Response> memberResponse = 회원_본인의_정보를_조회한다(accessTokenResponse.getAccessToken());
        MemberResponse responseResult = memberResponse.as(MemberResponse.class);

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(memberResponse);
            assertThat(responseResult.getId()).isEqualTo(1L);
        });
    }

    @DisplayName("프로필 정보로 회원가입을 하면 상태코드 204를 리턴한다.")
    @Test
    void 프로필_정보로_회원가입을_하면_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        // when
        ExtractableResponse<Response> resultResponse = 프로필_정보로_회원가입을_한다(accessTokenResponse.getAccessToken());

        // then
        assertAll(() -> {
            상태코드_204이_반환된다(resultResponse);
        });
    }
}
