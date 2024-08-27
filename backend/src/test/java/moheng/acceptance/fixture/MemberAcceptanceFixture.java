package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.member.domain.GenderType;
import moheng.member.dto.request.CheckDuplicateNicknameRequest;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.member.dto.request.UpdateProfileRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDate;

public class MemberAcceptanceFixture {

    public static ExtractableResponse<Response> 회원_본인의_정보를_조회한다(final String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/member/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 프로필_정보로_회원가입을_한다(final String accessToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(new SignUpProfileRequest("devhaon", LocalDate.of(2000, 1, 1), GenderType.MEN, "https://profile-image.com"))
                .when().post("/api/member/signup/profile")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    public static ExtractableResponse<Response> 닉네임을_중복_체크한다(final String accessToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(new CheckDuplicateNicknameRequest("devhaon"))
                .when().post("/api/member/check/nickname")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public static ExtractableResponse<Response> 회원_프로필을_업데이트한다(final String accessToken) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(new UpdateProfileRequest("devhaon", LocalDate.of(2000, 1, 1), GenderType.MEN, "https://image.com"))
                .when().put("/api/member/profile")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
