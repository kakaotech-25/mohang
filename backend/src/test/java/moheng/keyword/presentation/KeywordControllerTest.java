package moheng.keyword.presentation;

import moheng.auth.exception.InvalidOAuthServiceException;
import moheng.config.ControllerTestConfig;
import moheng.keyword.exception.InvalidAIServerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static moheng.fixture.AuthFixtures.토큰_생성_요청;
import static moheng.fixture.KeywordFixture.*;
import static moheng.fixture.MemberFixtures.*;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class KeywordControllerTest extends ControllerTestConfig {

    @DisplayName("키워드 기반 AI 여행지 추천을 받고 상태코드 200을 리턴한다.")
    @Test
    void 키워드_기반_AI_여행지_추천을_받고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(keywordService.findRecommendTripsByKeywords(anyLong(), any())).willReturn(키워드_기반_추천_여행지_응답());

        // when, then
        mockMvc.perform(get("/keyword/travel/model")
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
                                fieldWithPath("findTripResponses").description("AI 가 추천한 키워드 기반 추천 여행지 리스트"),
                                fieldWithPath("findTripResponses[].name").description("세부 여행지 이름"),
                                fieldWithPath("findTripResponses[].placeName").description("세부 여행지 장소명"),
                                fieldWithPath("findTripResponses[].contentId").description("세부 여행지 contentId"),
                                fieldWithPath("findTripResponses[].tripImageUrl").description("세부 여행지 이미지 경로"),
                                fieldWithPath("findTripResponses[].description").description("세부 여행지 설명")
                        )
                )).andExpect(status().isOk());
    }

    @DisplayName("키워드를 생성하고 상태코드 200을 리턴한다.")
    @Test
    void 키워드를_생성하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(keywordService).createKeyword(any());

        // when, then
        mockMvc.perform(get("/keyword/travel/model")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(키워드_생성_요청()))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("AI 서버에 예기치 못한 문제가 발생하면 상태코드 500을 리턴한다.")
    @Test
    void AI_서버에_예기치_못한_문제가_발생하면_상태코드_500을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(keywordService.findRecommendTripsByKeywords(anyLong(), any())).willThrow(new InvalidAIServerException("AI 서버에 예기치 못한 오류가 발생했습니다."));

        // when, then
        mockMvc.perform(get("/keyword/travel/model")
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
                        )
                )).andExpect(status().isInternalServerError());
    }
}
