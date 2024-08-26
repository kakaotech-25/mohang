package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import moheng.liveinformation.dto.UpdateMemberLiveInformationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

public class MemberLiveInfoAcceptenceFixture {
    public static ExtractableResponse<Response> 회원의_생활정보를_수정한다(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UpdateMemberLiveInformationRequest(List.of(1L, 2L)))
                .when().put("/api/live/info/member")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
