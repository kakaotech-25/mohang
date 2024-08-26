package moheng.keyword.presentation;

import moheng.config.slice.ControllerTestConfig;
import moheng.keyword.exception.InvalidAIServerException;
import moheng.trip.dto.FindTripsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static moheng.fixture.KeywordFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class KeywordControllerTest extends ControllerTestConfig {

    @DisplayName("키워드 기반 여행지를 추천을 받고 상태코드 200을 리턴한다.")
    @Test
    void 키워드_기반_여행지를_추천을_받고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(keywordService.findRecommendTripsByKeywords(any())).willReturn(키워드_기반_추천_여행지_응답());

        // when, then
        mockMvc.perform(post("/api/keyword/trip/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(키워드_기반_추천_여행지_요청()))
                )
                .andDo(print())
                .andDo(document("keyword/travel/model",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("keywordIds").description("키워드 ID 리스트")
                        ),
                        responseFields(
                                fieldWithPath("findTripResponses").description("키워드 기반 추천 여행지 결과 리스트"),
                                fieldWithPath("findTripResponses[].name").description("세부 여행지 이름"),
                                fieldWithPath("findTripResponses[].placeName").description("세부 여행지 장소명"),
                                fieldWithPath("findTripResponses[].contentId").description("세부 여행지 contentId"),
                                fieldWithPath("findTripResponses[].tripImageUrl").description("세부 여행지 이미지 경로"),
                                fieldWithPath("findTripResponses[].description").description("세부 여행지 설명"),
                                fieldWithPath("findTripResponses[].keywords").description("세부 여행지 키워드 리스트")
                        )
                )).andExpect(status().isOk());
    }

    @DisplayName("키워드를 생성하고 상태코드 200을 리턴한다.")
    @Test
    void 키워드를_생성하고_상태코드_200을_리턴한다() throws Exception {
        // given
        doNothing().when(keywordService).createKeyword(any());

        // when, then
        mockMvc.perform(post("/api/keyword")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(키워드_생성_요청()))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("여행지의 키워드를 생성하고 상태코드 204를 리턴한다.")
    @Test
    void 여행지의_키워드를_생성하고_상태코드_204를_리턴한다() throws Exception {
        // given
        doNothing().when(keywordService).createTripKeyword(any());

        // when, then
        mockMvc.perform(post("/api/keyword/trip")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(여행지_키워드_생성_요청()))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("무작위 랜덤 키워드로 추천 여행지를 찾고 상태코드 200을 리턴한다.")
    @Test
    void 무작위_랜덤_키워드로_추천_여행지를_찾고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(keywordService.findRecommendTripsByRandomKeyword())
                .willReturn(키워드_기반_추천_여행지_응답());

        // when, then
        mockMvc.perform(get("/api/keyword/random/trip")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("keyword/random/trip",
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
}
