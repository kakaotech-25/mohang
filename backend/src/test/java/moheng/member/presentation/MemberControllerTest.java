package moheng.member.presentation;

import static moheng.fixture.MemberFixtures.*;

import moheng.auth.domain.oauth.Authority;
import moheng.auth.exception.InvalidInitAuthorityException;
import moheng.auth.exception.InvalidOAuthServiceException;
import moheng.config.slice.ControllerTestConfig;
import moheng.liveinformation.exception.EmptyLiveInformationException;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import moheng.member.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Optional;

import static moheng.fixture.MemberFixtures.하온_기존;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
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
        mockMvc.perform(get("/api/member/me")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("member/me/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("id").description("고유 id 값"),
                                fieldWithPath("profileImageUrl").description("프로필 이미지 경로"),
                                fieldWithPath("nickname").description("성별. 형식: MEN 또는 WOMEN"),
                                fieldWithPath("birthday").description("생년월일. 형식:yyyy-MM-dd"),
                                fieldWithPath("genderType").description("프로필 이미지 경로.")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("존재하지 않는 멤버의 회원 정보를 조회하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_멤버의_회원_정보를_조회하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberService.findById(anyLong()))
                .willThrow(new NoExistMemberException("존재하지 않는 회원입니다."));

        // when, then
        mockMvc.perform(get("/api/member/me")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("member/me/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("프로필 정보로 회원가입에 성공하면 상태코드 204을 리턴한다.")
    @Test
    void 프로필_정보로_회원가입에_성공하면_상태코드_204을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));

        // when, then
        mockMvc.perform(post("/api/member/signup/profile")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(프로필_정보로_회원가입_요청()))
                )
                .andDo(print())
                .andDo(document("member/signup/profile/success",
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

    @DisplayName("프로필 정보로 회원가입시 입력한 닉네임의 길이가 유효범위를 벗어나면 상태코드 400을 리턴한다.")
    @Test
    void 프로필_정보로_회원가입시_입력한_닉네임의_길이가_유효범위를_벗어나면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));
        doThrow(new InvalidNicknameFormatException("이름은 1자 이상 50이하만 허용합니다."))
                .when(memberService).signUpByProfile(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/member/signup/profile")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유효하지_않은_닉네임_프로필_정보로_회원가입_요청()))
                )
                .andDo(print())
                .andDo(document("member/signup/profile/fail/nickname",
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
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로필 정보로 회원가입시 입력한 소셜 로그인 제공처가 유효하지 않다면 상태코드 400을 리턴한다.")
    @Test
    void 프로필_정보로_회원가입시_입력한_소셜_로그인_재공처가_유효하지_않다면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));
        doThrow(new NoExistSocialTypeException("존재하지 않는 소셜 로그인 제공처입니다."))
                .when(memberService).signUpByProfile(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/member/signup/profile")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유효하지_않은_닉네임_프로필_정보로_회원가입_요청()))
                )
                .andDo(print())
                .andDo(document("member/signup/profile/fail/invalidSocialType",
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
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로필 정보로 회원가입시 입력한 생년월일 날짜가 현재 날짜보다 더 이후라면 상태코드 400을 리턴한다.")
    @Test
    void 프로필_정보로_회원가입시_입력한_생년월일_날짜가_현재_날짜보다_더_이후라면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));
        doThrow(new InvalidBirthdayException("생년월일은 현재 날짜보다 더 이후일 수 없습니다."))
                .when(memberService).signUpByProfile(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/member/signup/profile")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유효하지_않은_닉네임_프로필_정보로_회원가입_요청()))
                )
                .andDo(print())
                .andDo(document("member/signup/profile/fail/birthday",
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
                .andExpect(status().isBadRequest());
    }

    @DisplayName("프로필 정보로 회원가입시 입력한 성별이 유효하지 않다면 상태코드 400을 리턴한다.")
    @Test
    void 프로필_정보로_회원가입시_입력한_성별이_유효하지_않다면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));
        doThrow(new InvalidGenderFormatException("유효하지 않은 성별 입니다."))
                .when(memberService).signUpByProfile(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/member/signup/profile")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유효하지_않은_닉네임_프로필_정보로_회원가입_요청()))
                )
                .andDo(print())
                .andDo(document("member/signup/profile/fail/genderType",
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
                .andExpect(status().isBadRequest());
    }

    @DisplayName("존재하지 않는 멤버가 프로필 정보로 회원가입 하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_멤버가_프로필_정보로_회원가입_하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistMemberException("존재하지 않는 회원입니다."))
                .when(memberService).findById(anyLong());

        // when, then
        mockMvc.perform(post("/api/member/signup/profile")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(프로필_정보로_회원가입_요청()))
                )
                .andDo(print())
                .andDo(document("member/signup/profile/fail/noExistMember",
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
                .andExpect(status().isNotFound());
    }


    @DisplayName("이미 회원가입을 마친 멤버가 프로필 정보로 회원가입을 요청하면 상태코드 403을 리턴한다.")
    @Test
    void 이미_회원가입을_마친_멤버가_프로필_정보로_회원가입에_요청하면_상태코드_403을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_기존()));
        doThrow(new InvalidInitAuthorityException("초기 회원가입 기능에 대한 접근 권한이 없습니다."))
                .when(memberService).checkIsAlreadyExistNickname(anyString());

        // when, then
        mockMvc.perform(post("/api/member/signup/profile")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(프로필_정보로_회원가입_요청()))
                )
                .andDo(print())
                .andDo(document("member/signup/authority/fail",
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
                .andExpect(status().isForbidden());
    }

    @DisplayName("중복되는 닉네임 없이 사용 가능한 닉네임이라면 메시지와 상태코드 200을 리턴한다.")
    @Test
    void 중복되는_닉네임_없이_사용_가능한_닉네임이라면_메시지와_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));

        // when, then
        mockMvc.perform(post("/api/member/check/nickname")
                .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(닉네임_중복확인_요청()))
        )
                .andDo(print())
                .andDo(document("member/check/nickname/success",
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
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));
        doThrow(new DuplicateNicknameException("중복되는 닉네임이 존재합니다."))
                .when(memberService).checkIsAlreadyExistNickname(anyString());

        // when, then
        mockMvc.perform(post("/api/member/check/nickname")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(닉네임_중복확인_요청()))
                )
                .andDo(print())
                .andDo(document("member/check/nickname/fail",
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

    @DisplayName("여행지 추천에 필요한 생활정보를 입력하여 회원가입에 성공하면 상태코드 204을 리턴한다.")
    @Test
    void 여행지_추천에_필요한_생활정보를_입력하여_회원가입에_성공하면_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));
        doNothing().when(memberService).signUpByLiveInfo(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/member/signup/liveinfo")
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
                                fieldWithPath("liveInfoIds").description("선택한 생활정보 고유 ID 값 리스트")
                        ))
                ).andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 생활정보를 선택하여 회원가입을 시도하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_생활정보를_선택하여_회원가입을_시도하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));
        doThrow(new NoExistLiveInformationException("존재하지 않는 생활정보입니다."))
                .when(memberService).signUpByLiveInfo(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/member/signup/liveinfo")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(생활정보로_회원가입_요청()))
                ).andDo(print())
                .andDo(document("member/signup/liveinfo/fail/noExistLiveInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("liveInfoIds").description("선택한 생활정보 고유 ID 값 리스트")
                        ))
                ).andExpect(status().isNotFound());
    }

    @DisplayName("관심 여행지를 선택하여 회원가입에 성공했다면 상태코드 204를 리턴한다.")
    @Test
    void 관심_여행지를_선택하여_회원가입에_성공했다면_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));

        // when, then
        mockMvc.perform(post("/api/member/signup/trip")
                .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(관심_여행지로_회원가입_요청()))
        ).andDo(print())
                .andDo(document("member/signup/trip/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("contentIds").description("관심 여행지의 contentId 리스트")
                        ))
                ).andExpect(status().isNoContent());
    }

    @DisplayName("관심 여행지를 선택을 잘못하여 회원가입에 실패했다면 상태코드 400을 리턴한다.")
    @Test
    void 관심_여행지_선택을_잘못하여_회원가입에_실패했다면_상태코드_400를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_신규()));
        doThrow(new ShortContentidsSizeException("AI 맞춤 추천을 위해 관심 여행지를 5개 이상, 10개 이하로 선택해야합니다."))
                .when(memberService).signUpByInterestTrips(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/member/signup/trip")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(잘못된_관심_여행지로_회원가입_요청()))
                ).andDo(print())
                .andDo(document("member/signup/trip/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("contentIds").description("관심 여행지의 contentId 리스트")
                        ))
                ).andExpect(status().isBadRequest());
    }

    @DisplayName("화원 프로필을 업데이트하면 상태코드 200을 리턴한다.")
    @Test
    void 회원_프로필을_업데이트하면_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);

        // when, then
        mockMvc.perform(put("/api/member/profile")
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
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(하온_기존()));
        doThrow(new DuplicateNicknameException("중복되는 닉네임이 존재합니다."))
                .when(memberService).updateByProfile(anyLong(), any());

        // when, then
        mockMvc.perform(put("/api/member/profile")
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
}
