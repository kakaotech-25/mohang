package moheng.auth.presentation;

import static moheng.fixture.AuthFixtures.토큰_생성_요청;
import static moheng.fixture.AuthFixtures.토큰_응답;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import moheng.auth.domain.token.MemberToken;
import moheng.auth.dto.RenewalAccessTokenResponse;
import moheng.auth.dto.TokenRequest;
import moheng.auth.dto.TokenResponse;
import moheng.config.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class AuthControllerTest extends ControllerTestConfig {
    @DisplayName("소셜 로그인을 위한 링크와 상태코드 200을 리턴한다.")
    @Test
    public void OAuth_소셜_로그인을_위한_링크와_상태코드_200을_리턴한다() throws Exception {
        // given
        given(authService.generateUri(anyString())).willReturn("URI");

        // when, then
        mockMvc.perform(get("/auth/{oAuthProvider}/link", "KAKAO"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("OAuth 로그인을 하면 토큰과 상태코드 200을 리턴한다.")
    @Test
    public void OAuth_로그인을_하면_토큰과_상태코드_200을_반환한다() throws Exception {
        // given
        given(authService.generateTokenWithCode(any(), any())).willReturn(토큰_응답());

        // when, then
        mockMvc.perform(post("/auth/{oAuthProvider}/login", "KAKAO")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(토큰_생성_요청())))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
