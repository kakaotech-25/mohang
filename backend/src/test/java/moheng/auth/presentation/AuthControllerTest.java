package moheng.auth.presentation;

import static moheng.fixture.MemberFixtures.*;
import static moheng.fixture.AuthFixtures.*;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.auth.domain.token.MemberToken;
import moheng.auth.dto.RenewalAccessTokenResponse;
import moheng.auth.dto.TokenRequest;
import moheng.auth.dto.TokenResponse;
import moheng.auth.exception.InvalidOAuthServiceException;
import moheng.auth.exception.InvalidTokenException;
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
                .andDo(document("auth/generate/link",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("oAuthProvider").description("KAKAO")),
                        responseFields(fieldWithPath("oAuthUri").type(JsonFieldType.STRING).description("OAuth 소셜 로그인 링크"))
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("OAuth 로그인을 하면 토큰과 상태코드 201을 리턴한다.")
    @Test
    public void OAuth_로그인을_하면_토큰과_상태코드_201을_반환한다() throws Exception {
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

    @DisplayName("OAuth 로그인 서버에 문제가 발생하면 상태코드 500을 반환한다.")
    @Test
    void OAuth_로그인에_문제가_발생하면_상태코드_500을_반환한다() throws Exception {
        // given
        given(authService.generateTokenWithCode(any(), any())).willThrow(new InvalidOAuthServiceException("카카오 OAuth 소셜 로그인 서버에 예기치 못한 오류가 발생했습니다."));

        // when, then
        mockMvc.perform(post("/auth/{provider}/token", "KAKAO")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(토큰_생성_요청())))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("리프레시 토큰을 통해 새로운 엑세스 토큰을 발급하면 상태코드 200을 리턴한다.")
    @Test
    void 리프레시_토큰을_통해_새로운_엑세스_토큰을_발급하면_상태코드_200을_리턴한다() throws Exception{
        // given
        given(authService.generateRenewalAccessToken(any())).willReturn(토큰_갱신_응답());

        // when, then
        mockMvc.perform(post("/auth/extend/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(토큰_갱신_요청())
        ).andDo(print()).andExpect(status().isCreated());
    }

    @DisplayName("만료되었거나 잘못 변형된 리프레시 토큰으로 새로운 엑세스 토큰을 재발급하려 하면 상태코드 401을 리턴한다.")
    @Test
    void 만료되었거나_잘못_변형된_리프레시_토큰으로_새로운_엑세스_토큰을_발급하려_하면_상태코드_401을_리턴한다() throws Exception {
        // given
        given(authService.generateRenewalAccessToken(any())).willThrow(new InvalidTokenException("변조되었거나 만료된 토큰 입니다."));

        // when, then
        mockMvc.perform(post("/auth/extend/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(토큰_갱신_요청())
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("로그아웃을 히면 리프레시 토큰을 삭제하고 HTTP 상태코드 값 200을 리턴한다.")
    @Test
    void 로그아웃을_히면_리프레시_토큰을_삭제하고_HTTP_상태코드_값_200을_리턴한다() throws Exception {
        // given, when, then
        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer aaaaaaaa.bbbbbbbb.cccccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
