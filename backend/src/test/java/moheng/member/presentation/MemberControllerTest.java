package moheng.member.presentation;

import static moheng.fixture.MemberFixtures.*;
import static moheng.fixture.AuthFixtures.*;

import moheng.auth.domain.token.JwtTokenProvider;
import moheng.auth.dto.TokenRequest;
import moheng.config.ControllerTestConfig;
import moheng.member.dto.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTestConfig {

    @DisplayName("사용자 본인의 회원 정보를 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 본인의_회원_정보를_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberService.findById(anyLong())).willReturn(하온_응답());

        // when, then
        mockMvc.perform(get("/member/me")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("member/me",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 ID"),
                                fieldWithPath("profileImageUrl").description("회원 프로필 이미지 URL"),
                                fieldWithPath("nickname").description("회원 닉네임"),
                                fieldWithPath("birthday").description("회원 생년월일"),
                                fieldWithPath("gender").description("회원 성별")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("프로필 정보로 회원가입에 성공하면 상태코드 204을 리턴한다.")
    @Test
    void 프로필_정보로_회원가입에_성공하면_상태코드_204을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);

        // when, then
        mockMvc.perform(post("/member/signup/profile")
                .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(프로필_정보로_회원가입_요청()))
        )
                .andDo(print())
                .andDo(document("member/signup/profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("birthday").description("생년월일. 형식:yyyy-MM-dd"),
                                fieldWithPath("genderType").description("성별. 형식: MEN 또는 WOMEN")
                        )
                ))
                .andExpect(status().isNoContent());
    }
}
