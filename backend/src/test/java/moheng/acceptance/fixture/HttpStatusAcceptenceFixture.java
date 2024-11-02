package moheng.acceptance.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpStatusAcceptenceFixture {
    public static void 상태코드_200이_반환된다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(org.springframework.http.HttpStatus.OK.value());
    }

    public static void 상태코드_201이_반환된다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(org.springframework.http.HttpStatus.CREATED.value());
    }

    public static void 상태코드_204이_반환된다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(org.springframework.http.HttpStatus.NO_CONTENT.value());
    }

    public static void 상태코드_401이_반환된다(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(org.springframework.http.HttpStatus.UNAUTHORIZED.value());
    }
}
