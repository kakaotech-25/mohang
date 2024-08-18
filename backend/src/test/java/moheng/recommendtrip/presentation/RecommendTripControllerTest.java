package moheng.recommendtrip.presentation;

import moheng.config.slice.ControllerTestConfig;
import moheng.keyword.exception.InvalidAIServerException;
import moheng.trip.dto.FindTripsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static moheng.fixture.RecommendTripFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
        mockMvc.perform(post("/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(선호_여행지_생성_요청()))
                )
                .andExpect(status().isNoContent());
    }

    @DisplayName("AI 맞춤 추천 여행지를 조회하고 상태코드 200을 리턴한다.")
    @Test
    void AI_맞춤_추천_여행지를_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(recommendTripService.findRecommendTripsByModel(anyLong()))
                .willReturn(AI_맞춤_추천_여행지_응답());

        // when, then
        mockMvc.perform(get("/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/recommend/ai/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("findTripResponses").description("여행지 리스트"),
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
        given(recommendTripService.findRecommendTripsByModel(anyLong()))
                .willThrow(new InvalidAIServerException("AI 서버에 예기치 못한 오류가 발생했습니다."));

        // when, then
        mockMvc.perform(get("/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/recommend/ai/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ))
                .andExpect(status().isInternalServerError());
    }
}
