package moheng.recommendtrip.presentation;

import moheng.config.slice.ControllerTestConfig;
import moheng.keyword.exception.InvalidAIServerException;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.exception.LackOfRecommendTripException;
import moheng.recommendtrip.exception.NoExistMemberTripException;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static moheng.fixture.RecommendTripFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecommendTripControllerTest extends ControllerTestConfig {

    @DisplayName("사용자의 선호 여행지 정보를 저장하고 상태코드 204을 리턴한다.")
    @Test
    void 사용자의_선호_여행지_정보를_저장하고_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(recommendTripService).createRecommendTrip(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(선호_여행지_생성_요청(1L)))
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 멤버의 선호 여행지를 저장하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_멤버의_선호_여행지를_저장하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistMemberException("존재하지 않는 회원입니다."))
                .when(recommendTripService).createRecommendTrip(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(선호_여행지_생성_요청(1L)))
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("존재하지 않는 여행지를 선호 여행지로 선택하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_여행지를_선호_여행지로_선택하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripException("존재하지 않는 여행지입니다."))
                .when(recommendTripService).createRecommendTrip(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(선호_여행지_생성_요청(-1L)))
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("AI 맞춤 추천 여행지를 조회하고 상태코드 200을 리턴한다.")
    @Test
    void AI_맞춤_추천_여행지를_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(tripFilterStrategyProvider.findTripsByFilterStrategy(anyString()))
                .willReturn(tripFilterStrategy);
        given(tripFilterStrategy.execute(any()))
                .willReturn(List.of());
        given(tripsWithKeywordProvider.findWithKeywords(any()))
                .willReturn(AI_맞춤_추천_여행지_응답());

        // when, then
        mockMvc.perform(get("/api/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/recommend/ai/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("findTripResponses").description("여행지 리스트"),
                                fieldWithPath("findTripResponses[].tripId").description("세부 여행지 고유 ID"),
                                fieldWithPath("findTripResponses[].name").description("세부 여행지 이름"),
                                fieldWithPath("findTripResponses[].placeName").description("세부 여행지 장소명"),
                                fieldWithPath("findTripResponses[].contentId").description("세부 여행지 contentId"),
                                fieldWithPath("findTripResponses[].tripImageUrl").description("세부 여행지 이미지 경로"),
                                fieldWithPath("findTripResponses[].description").description("세부 여행지 설명"),
                                fieldWithPath("findTripResponses[].keywords").description("세부 여행지 키워드 리스트")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("AI 맞춤 추천 여행지를 조회시 AI 서버에 문제가 발생하면 상태코드 500을 리턴한다.")
    @Test
    void AI_맞춤_추천_여행지를_조회시_AI_서버에_문제가_발생하면_상태코드_500을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(tripFilterStrategyProvider.findTripsByFilterStrategy(anyString()))
                .willThrow(new InvalidAIServerException("AI 서버에 예기치 못한 오류가 발생했습니다."));

        // when, then
        mockMvc.perform(get("/api/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/recommend/ai/fail/server",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("존재하지 않는 멤버가 AI 맞춤 추천 여행지 조회시 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_멤버가_AI_맞춤_추천_여행지_조회시_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(tripFilterStrategyProvider.findTripsByFilterStrategy(anyString()))
                .willThrow(new NoExistMemberException("존재하지 않는 회원입니다."));

        // when, then
        mockMvc.perform(get("/api/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/recommend/ai/fail/member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("AI 맞춤 여행지 추천을 받기위한 멤버의 선호 여행지 데이터 수가 5개 미만으로 부족하다면 상태코드 422를 리턴한다.")
    @Test
    void AI_맞춤_여행지_추천을_받기위한_멤버의_선호_여행지_데이터_수가_5개_미만으로_부족하다면_상태코드_422를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(tripFilterStrategyProvider.findTripsByFilterStrategy(anyString()))
                .willThrow(new LackOfRecommendTripException("추천을 받기위한 선호 여행지 데이터 수가 부족합니다."));

        // when, then
        mockMvc.perform(get("/api/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/recommend/ai/fail/lackOfPreferredTrip",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isUnprocessableEntity());
    }

    @DisplayName("AI 맞춤 여행지 추천시 멤버의 선호 여행지가 없다면 상태코드 404를 리턴한다.")
    @Test
    void AI_맞춤_여행지_추천시_멤버의_선호_여행지가_없다면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(tripFilterStrategyProvider.findTripsByFilterStrategy(anyString()))
                .willThrow(new NoExistMemberTripException("존재하지 않는 멤버의 선호 여행지입니다."));

        // when, then
        mockMvc.perform(get("/api/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/recommend/ai/fail/noExistPreferredTrip",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isNotFound());
    }
}
