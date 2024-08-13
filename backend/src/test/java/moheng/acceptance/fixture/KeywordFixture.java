package moheng.acceptance.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import org.springframework.http.MediaType;

public class KeywordFixture {
    public static ExtractableResponse<Response> 키워드를_생성한다(String name) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new KeywordCreateRequest(name))
                .when().post("/keyword")
                .then().log().all()
                .statusCode(org.springframework.http.HttpStatus.NO_CONTENT.value())
                .extract();
    }
}
