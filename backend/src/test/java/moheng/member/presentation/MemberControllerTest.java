package moheng.member.presentation;

import static moheng.fixture.MemberFixtures.*;
import static moheng.fixture.AuthFixtures.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import moheng.auth.domain.token.JwtTokenProvider;
import moheng.auth.dto.TokenRequest;
import moheng.auth.exception.InvalidTokenException;
import moheng.config.ControllerTestConfig;
import moheng.liveinformation.exception.EmptyLiveInformationException;
import moheng.member.dto.response.MemberResponse;
import moheng.member.exception.DuplicateNicknameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                                fieldWithPath("genderType").description("성별. 형식: MEN 또는 WOMEN"),
                                fieldWithPath("profileImageUrl").description("프로필 이미지 경로.")
                        )
                ))
                .andExpect(status().isNoContent());
    }

    @DisplayName("중복되는 닉네임 없이 사용 가능한 닉네임이라면 메시지와 상태코드 200을 리턴한다.")
    @Test
    void 중복되는_닉네임_없이_사용_가능한_닉네임이라면_메시지와_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);

        // when, then
        mockMvc.perform(post("/member/check/nickname")
                .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(닉네임_중복확인_요청()))
        )
                .andDo(print())
                .andDo(document("member/check/nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("message").description("사용 가능한 닉네임입니다.")
                        )
                )).andExpect(status().isOk());
    }

    @DisplayName("중복되는 닉네임 존재한다면 상태코드 401을 리턴한다.")
    @Test
    void 중복되는_닉네임이_존재한다면_상태코드_401을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new DuplicateNicknameException("중복되는 닉네임이 존재합니다."))
                .when(memberService).checkIsAlreadyExistNickname(anyString());

        // when, then
        mockMvc.perform(post("/member/check/nickname")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(닉네임_중복확인_요청()))
                )
                .andDo(print())
                .andDo(document("member/check/nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("message").description("중복되는 닉네임이 존재합니다.")
                        )
                )).andExpect(status().isUnauthorized());
    }

    @DisplayName("화원 프로필을 업데이트하면 상태코드 200을 리턴한다.")
    @Test
    void 회원_프로필을_업데이트하면_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);

        // when, then
        mockMvc.perform(put("/member/profile")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(프로필_업데이트_요청()))
        )
                .andDo(print())
                .andDo(document("member/update/profile/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("birthday").description("생년월일"),
                                fieldWithPath("genderType").description("성별"),
                                fieldWithPath("profileImageUrl").description("프로필 이미지 경로")
                        )
                )).andExpect(status().isNoContent());
    }

    @DisplayName("회원 프로필 업데이트시 본인을 제외한 중복 닉네임이 존재한다면 상태코드 401을 리턴한다.")
    @Test
    void 회원_프로필_업데이트시_본인을_제외한_중복_닉네임이_존재한다면_상태코드_401을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new DuplicateNicknameException("중복되는 닉네임이 존재합니다."))
                .when(memberService).updateByProfile(anyLong(), any());

        // when, then
        mockMvc.perform(put("/member/profile")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(프로필_업데이트_요청()))
                )
                .andDo(print())
                .andDo(document("member/update/profile/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("닉네임"),
                                fieldWithPath("birthday").description("생년월일"),
                                fieldWithPath("genderType").description("성별"),
                                fieldWithPath("profileImageUrl").description("프로필 이미지 경로")
                        )
                )).andExpect(status().isUnauthorized());
    }

    @DisplayName("여행지 추천에 필요한 생활정보를 입력하여 회원가입에 성공하면 상태코드 204을 리턴한다.")
    @Test
    void 여행지_추천에_필요한_생활정보를_입력하여_회원가입에_성공하면_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(memberService).signUpByLiveInfo(anyLong(), any());

        // when, then
        mockMvc.perform(post("/member/signup/liveinfo")
                .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(생활정보로_회원가입_요청()))
        ).andDo(print())
                .andDo(document("member/signup/liveinfo/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("liveInfoNames").description("선택한 생활정보 이름 리스트")
                        ))
                ).andExpect(status().isNoContent());
    }

    @DisplayName("전달받은 여행지 추천에 필요한 생활정보 리스트가 비어있거나 유효하지 않다면 상태코드 400을 리턴한다.")
    @Test
    void 전달받은_여행지_추천에_필요한_생활정보_리스트가_비어있거나_유효하지_않다면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new EmptyLiveInformationException("생활정보를 선택하지 않았습니다."))
                .when(memberService).signUpByLiveInfo(anyLong(), any());

        // when, then
        mockMvc.perform(post("/member/signup/liveinfo")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(비어있는_생활정보로_회원가입_요청()))
                ).andDo(print())
                .andDo(document("member/signup/liveinfo/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("liveInfoNames").description("선택한 생활정보 이름 리스트")
                        ))
                ).andExpect(status().isBadRequest());
    }

    @DisplayName("관심 여행지를 선택하여 회원가입에 성공했다면 상태코드 204를 리턴한다.")
    @Test
    void 관심_여행지를_입력하여_회원가입에_성공했다면_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);

        // when, then
        mockMvc.perform(post("/member/signup/liveinfo")
                .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(관심_여행지로_회원가입_요청()))
        ).andDo(print())
                .andExpect(status().isNoContent());
    }
}
