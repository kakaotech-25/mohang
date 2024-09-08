package moheng.acceptance;

import static moheng.acceptance.fixture.MemberAcceptanceFixture.*;
import static moheng.acceptance.fixture.TripAcceptenceFixture.*;
import static moheng.acceptance.fixture.LiveInfoAcceptenceFixture.*;
import static moheng.acceptance.fixture.AuthAcceptanceFixture.*;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.상태코드_200이_반환된다;
import static moheng.acceptance.fixture.HttpStatusAcceptenceFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.acceptance.config.AcceptanceTestConfig;
import moheng.auth.dto.AccessTokenResponse;
import moheng.liveinformation.dto.FindMemberLiveInformationResponses;
import moheng.member.dto.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @DisplayName("중복 닉네임 체크하여 사용 가능한 닉네임이라면 상태코드 200을 리턴한다.")
    @Test
    void 중복_닉네임을_체크하여_사용_가능한_닉네임이라면_상태코드_200을_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        // when
        ExtractableResponse<Response> resultResponse = 닉네임을_중복_체크한다(accessTokenResponse.getAccessToken());

        // then
        assertAll(() -> {
            상태코드_200이_반환된다(resultResponse);
        });
    }

    @DisplayName("회원 프로필을 업데이트하면 상태코드 204를 리턴한다.")
    @Test
    void 회원_프로필을_업데이트하면_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        프로필_정보로_회원가입을_한다(accessTokenResponse.getAccessToken());

        // when
        ExtractableResponse<Response> resultResponse = 회원_프로필을_업데이트한다(accessTokenResponse.getAccessToken());

        // then
        assertAll(() -> {
            상태코드_204이_반환된다(resultResponse);
        });
    }

    @DisplayName("생활정보를 입력하여 회원가입 하면 상태코드 204를 리턴한다.")
    @Test
    void 생활정보를_입력하여_회원가입_하면_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        생활정보를_생성한다("생활정보1");
        생활정보를_생성한다("생활정보2");

        // when
        ExtractableResponse<Response> resultResponse = 생활정보로_회원가입_한다(accessTokenResponse);

        // then
        assertAll(() -> {
            상태코드_204이_반환된다(resultResponse);
        });
    }

    @DisplayName("관심 여행지를 입력하여 회원가입 하면 상태코드 204를 리턴한다.")
    @Test
    void 관심_여행지를_입력하여_회원가입_하면_상태코드_204를_리턴한다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        여행지를_생성한다("롯데월드", 1L);
        여행지를_생성한다("경복궁", 2L);
        여행지를_생성한다("덕포진", 3L);
        여행지를_생성한다("해운대", 4L);
        여행지를_생성한다("광안리 해수욕장", 5L);

        // when
        ExtractableResponse<Response> resultResponse = 관심_여행지로_회원가입_한다(accessTokenResponse);

        // then
        assertAll(() -> {
            상태코드_204이_반환된다(resultResponse);
        });
    }

    @DisplayName("소셜 로그인 후 최초 회원가입을 마친 멤버의 모든 프로필 정보는 null 일 수 없다.")
    @Test
    void 소셜_로그인_후_최초_회원가입을_마친_멤버의_모든_프로필_정보는_null_일_수_없다() {
        // given, when
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);
        프로필_정보로_회원가입을_한다(accessTokenResponse.getAccessToken());

        // then
        ExtractableResponse<Response> memberResponse = 회원_본인의_정보를_조회한다(accessTokenResponse.getAccessToken());
        MemberResponse resultResponse = memberResponse.as(MemberResponse.class);

        assertAll(() -> {
            assertThat(resultResponse.getId()).isNotNull();
            assertThat(resultResponse.getBirthday()).isNotNull();
            assertThat(resultResponse.getNickname()).isNotNull();
            assertThat(resultResponse.getProfileImageUrl()).isNotNull();
            assertThat(resultResponse.getGenderType()).isNotNull();
            assertThat(memberResponse.statusCode()).isEqualTo(200);
        });
    }

    @DisplayName("소셜 로그인 후 최초 회원가입을 마친 멤버의 프로필 정보와 생활정보 정보는 비어있을 수 없다.")
    @Test
    void 소셜_로그인_후_최초_회원가입을_마친_멤버의_생활정보는_비어있을_수_없다() {
        // given
        ExtractableResponse<Response> response = 자체_토큰을_생성한다("KAKAO", "authorization-code");
        AccessTokenResponse accessTokenResponse = response.as(AccessTokenResponse.class);

        생활정보를_생성한다("생활정보1"); 생활정보를_생성한다("생활정보2");
        생활정보를_생성한다("생활정보3"); 생활정보를_생성한다("생활정보4"); 생활정보를_생성한다("생활정보5");

        프로필_정보로_회원가입을_한다(accessTokenResponse.getAccessToken());
        생활정보로_회원가입_한다(accessTokenResponse);

        // when
        ExtractableResponse<Response> memberResponse = 회원_본인의_정보를_조회한다(accessTokenResponse.getAccessToken());
        MemberResponse profileResponse = memberResponse.as(MemberResponse.class);

        ExtractableResponse<Response> liveInfoResponse = 멤버의_생활정보를_찾는다(accessTokenResponse);
        FindMemberLiveInformationResponses memberLiveInformationResponses = liveInfoResponse.as(FindMemberLiveInformationResponses.class);

        // then
        assertAll(() -> {
            assertThat(profileResponse.getId()).isNotNull();
            assertThat(profileResponse.getBirthday()).isNotNull();
            assertThat(profileResponse.getNickname()).isNotNull();
            assertThat(profileResponse.getProfileImageUrl()).isNotNull();
            assertThat(profileResponse.getGenderType()).isNotNull();
            assertThat(memberLiveInformationResponses.getLiveInfoResponses()).hasSize(5);
        });
    }
}
