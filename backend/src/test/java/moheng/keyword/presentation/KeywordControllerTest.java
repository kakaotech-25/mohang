package moheng.keyword.presentation;

import moheng.auth.exception.InvalidInitAuthorityException;
import moheng.config.slice.ControllerTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.dto.FindAllKeywordResponse;
import moheng.keyword.dto.FindAllKeywordResponses;
import moheng.keyword.exception.InvalidAIServerException;
import moheng.keyword.exception.KeywordNameLengthException;
import moheng.keyword.exception.NoExistKeywordException;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static moheng.fixture.KeywordFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

    @DisplayName("모든 키워드를 찾고 상태코드 200을 리턴한다.")
    @Test
    void 모든_키워드를_찾고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(keywordService.findAllKeywords()).willReturn(new FindAllKeywordResponses(
                List.of(new Keyword("키워드1"), new Keyword("키워드2"), new Keyword("키워드3"))
        ));

        // when, then
        mockMvc.perform(get("/api/keyword")
                .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andDo(document("keyword/find/all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("findAllKeywordResponses").description("키워드 리스트"),
                                fieldWithPath("findAllKeywordResponses[].keywordId").description("키워드 고유 ID 값"),
                                fieldWithPath("findAllKeywordResponses[].name").description("키워드 이름")
                        )
                )).andExpect(status().isOk());
    }

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
                .andDo(document("keyword/travel/recommend/success",
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

    @DisplayName("키워드 기반 추천 여행지를 받을때 일부 존재하지 않는 키워드가 존재하는 경우 상태코드 404를 리턴한다.")
    @Test
    void 키워드_기반_추천_여행지를_받을때_일부_존재하지_않는_키워드가_존재하는_경우_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistKeywordException("일부 키워드가 존재하지 않습니다."))
                .when(keywordService).findRecommendTripsByKeywords(any());

        // when, then
        mockMvc.perform(post("/api/keyword/trip/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(키워드_기반_추천_여행지_요청()))
                )
                .andDo(print())
                .andDo(document("keyword/travel/recommend/fail/noExistKeyword",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("keywordIds").description("키워드 ID 리스트")
                        )
                )).andExpect(status().isNotFound());
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

    @DisplayName("생성할 키워드 이름의 길이가 허용범위를 벗어나면 상태코드 400을 리턴한다.")
    @Test
    void 생성할_키워드_이름의_길이가_허용범위를_벗어나면_상태코드_400을_리턴한다() throws Exception {
        // given
        doNothing().when(keywordService).createKeyword(any());
        doThrow(new KeywordNameLengthException("키워드 이름의 길이는 최소 2자 이상, 최대 100자 이하만 허용합니다."))
                .when(keywordService).createKeyword(any());

        // when, then
        mockMvc.perform(post("/api/keyword")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(키워드_생성_요청()))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
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


    @DisplayName("존재하지 않는 여행지의 여행 키워드를 생성하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_여행지의_여행_키워드를_생성하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripException("존재하지 않는 여행지입니다."))
                .when(keywordService).createTripKeyword(any());

        // when, then
        mockMvc.perform(post("/api/keyword/trip")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(키워드_생성_요청()))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("존재하지 않는 키워드의 여행 키워드를 생성하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_키워드의_여행_키워드를_생성하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistKeywordException("존재하지 않는 키워드입니다."))
                .when(keywordService).createTripKeyword(any());

        // when, then
        mockMvc.perform(post("/api/keyword/trip")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(키워드_생성_요청()))
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("무작위 랜덤 키워드로 추천 여행지를 찾고 상태코드 200을 리턴한다.")
    @Test
    void 무작위_랜덤_키워드로_추천_여행지를_찾고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(keywordService.findRecommendTripsByRandomKeyword())
                .willReturn(랜덤_키워드_기반_추천_여행지_응답());

        // when, then
        mockMvc.perform(get("/api/keyword/random/trip")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andDo(document("keyword/random/trip/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("keywordName").description("랜덤으로 선택된 현재 키워드 이름"),
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

    @DisplayName("랜덤 키워드를 찾을 수 없다면 상태코드 401을 리턴한다.")
    @Test
    void 랜덤_키워드를_찾을_수_없다면_상태코드_401을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistKeywordException("랜덤 키워드를 찾을 수 없습니다."))
                .when(keywordService).findRecommendTripsByKeywords(any());

        // when, then
        mockMvc.perform(post("/api/keyword/trip/recommend")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(키워드_기반_추천_여행지_요청()))
                )
                .andDo(print())
                .andDo(document("keyword/random/trip/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("keywordIds").description("키워드 ID 리스트")
                        )
                )).andExpect(status().isNotFound());
    }
}
