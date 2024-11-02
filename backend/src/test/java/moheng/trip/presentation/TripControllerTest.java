package moheng.trip.presentation;

import static moheng.fixture.MemberFixtures.프로필_정보로_회원가입_요청;
import static moheng.fixture.MemberFixtures.하온_신규;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static moheng.fixture.TripFixture.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import moheng.config.slice.ControllerTestConfig;
import moheng.keyword.exception.InvalidAIServerException;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripWithSimilarTripsResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.exception.NoExistRecommendTripException;
import moheng.trip.exception.NoExistRecommendTripStrategyException;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;
import java.util.Optional;

public class TripControllerTest extends ControllerTestConfig {

    @DisplayName("여행지를 생성하면 상태코드 204를 리턴한다.")
    @Test
    void 여행지를_생성하면_상태코드_204를_리턴한다() throws Exception {
        // given, when, then
        mockMvc.perform(post("/api/trip")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(롯데월드_여행지_생성_요청()))
                )
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("상위 30개의 여행지들을 방문 수 기준으로 조회한다.")
    @Test
    void 상위_30개의_여행지들을_방문_수_기준으로_정렬한다() throws Exception {
        // given
        given(tripService.findTop30OrderByVisitedCountDesc())
                .willReturn(여행지_응답());

        // when, then
        mockMvc.perform(get("/api/trip/find/interested")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andDo(document("trip/find/interested",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("findTripResponses").description("여행지 리스트"),
                                fieldWithPath("findTripResponses[].tripId").description("세부 여행지 고유 ID 값"),
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

    @DisplayName("현재 여행지를 비슷한 여행지와 함께 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 현재_여행지를_비슷한_여행지와_함께_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(tripService.findWithSimilarOtherTrips(anyLong(), anyLong()))
                .willReturn(여행지_조회_응답());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/trip/find/{tripId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/find/current",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("tripId").description("여행지 고유 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("findTripResponse.tripId").description("선택한 여행지의 고유 ID 값"),
                                fieldWithPath("findTripResponse.name").description("선택한 여행지의 이름"),
                                fieldWithPath("findTripResponse.placeName").description("선택한 여행지의 장소명"),
                                fieldWithPath("findTripResponse.contentId").description("선택한 여행지의 컨텐츠 ID"),
                                fieldWithPath("findTripResponse.tripImageUrl").description("선택한 여행지의 이미지 URL"),
                                fieldWithPath("findTripResponse.description").description("선택한 여행지에 대한 설명"),
                                fieldWithPath("findTripResponse.keywords[]").description("선택한 여행지와 관련된 키워드 목록"),
                                fieldWithPath("similarTripResponses.findTripResponses[].tripId").description("유사한 여행지의 고유 ID 값"),
                                fieldWithPath("similarTripResponses.findTripResponses[].name").description("유사한 여행지의 이름"),
                                fieldWithPath("similarTripResponses.findTripResponses[].placeName").description("유사한 여행지의 장소명"),
                                fieldWithPath("similarTripResponses.findTripResponses[].contentId").description("유사한 여행지의 컨텐츠 ID"),
                                fieldWithPath("similarTripResponses.findTripResponses[].tripImageUrl").description("유사한 여행지의 이미지 URL"),
                                fieldWithPath("similarTripResponses.findTripResponses[].description").description("유사한 여행지에 대한 설명"),
                                fieldWithPath("similarTripResponses.findTripResponses[].keywords[]").description("유사한 여행지와 관련된 키워드 목록")
                        )
                ))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("존재하지 않는 여행지를 조회하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_여행지를_조회하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripException("존재하지 않는 여행지입니다."))
                .when(tripService).findWithSimilarOtherTrips(anyLong(), anyLong());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/trip/find/{tripId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/find/current/fail/noExistTrip",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("tripId").description("여행지 고유 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("현재 여행지를 비슷한 여행지와 함께 조회시 AI 서버에 예기치 못한 문제가 발생했다면 상태코드 500을 리턴한다.")
    @Test
    void 현재_여행지를_비슷한_여행지와_함께_조회시_AI_서버에_예기치_못한_문제가_발생했다면_상태코드_500을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new InvalidAIServerException("AI 서버에 예기치 못한 오류가 발생했습니다."))
                .when(tripService).findWithSimilarOtherTrips(anyLong(), anyLong());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/trip/find/{tripId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/find/current/fail/aiServer",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("tripId").description("여행지 고유 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isInternalServerError());
    }

    @DisplayName("멤버의 선호 여행지 정보가 아예 없어서 현재 방문한 여행지를 선호 여행지로 추가할 수 없다면 상태코드 422를 리턴한다.")
    @Test
    void 멤버의_선호_여행지_정보가_아예_없어서_현재_방문한_여행지를_선호_여행지로_추가할_수_없다면_상태코드_422를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistRecommendTripException("선호 여행지 정보가 없습니다."))
                .when(tripService).findWithSimilarOtherTrips(anyLong(), anyLong());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/trip/find/{tripId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/find/current/fail/noExistRecommendTrip",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("tripId").description("여행지 고유 ID")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isUnprocessableEntity());
    }

    @DisplayName("멤버의 선호 여행지 갯수에 따른 적절한 선호 여행지 저장 전략이 존재하지 않는다면 상태코드 422를 리턴한다.")
    @Test
    void 멤버의_선호_여행지_갯수에_따른_적절한_선호_여행지_저장_전략이_존재하지_않는다면_상태코드_422를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistRecommendTripStrategyException("제공되지 않는 선호 여행지 저장 전략입니다."))
                .when(tripService).findWithSimilarOtherTrips(anyLong(), anyLong());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/trip/find/{tripId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(document("trip/find/current/fail/noExistRecommendTripStrategy",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("tripId").description("여행지 고유 ID")
                        )
                ))
                .andExpect(status().isUnprocessableEntity());
    }

    @DisplayName("멤버의 여행지를 생성하고 상태코드 200을 리턴한다.")
    @Test
    void 멤버의_여행지를_생성하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(tripService).createMemberTrip(anyLong(), anyLong());

        // when, then
        mockMvc.perform(post("/api/trip/member/{tripId}", 1L)
                .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 여행지로 멤버의 선호 여행지를 선택하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_여행지로_멤버의_선호_여행지를_선택하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripException("존재하지 않는 여행지입니다."))
                .when(tripService).createMemberTrip(anyLong(), anyLong());

        // when, then
        mockMvc.perform(post("/api/trip/member/{tripId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
