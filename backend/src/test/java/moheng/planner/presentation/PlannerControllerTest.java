package moheng.planner.presentation;

import static moheng.fixture.PlannerFixture.*;
import static moheng.fixture.TripScheduleFixtures.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import moheng.config.slice.ControllerTestConfig;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import moheng.planner.exception.NoExistTripScheduleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

public class PlannerControllerTest extends ControllerTestConfig {

    @DisplayName("플래너의 여행지를 최신순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 플래너의_여행지를_최신순으로_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(plannerService.findPlannerOrderByRecent(anyLong())).willReturn(플래너_최신순_조회_응답());

        // when, then
        mockMvc.perform(get("/api/planner/recent")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/find/recent",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("tripScheduleResponses").description("여행 일정 리스트 : 최신순조회"),
                                fieldWithPath("tripScheduleResponses[].scheduleId").description("여행 일정 고유 ID 값"),
                                fieldWithPath("tripScheduleResponses[].scheduleName").description("여행 일정 이름"),
                                fieldWithPath("tripScheduleResponses[].startTime").description("여행 일정 시작날짜"),
                                fieldWithPath("tripScheduleResponses[].endTime").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isOk());
    }

    @DisplayName("플래너의 여행지를 이름순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 플래너의_여행지를_이름순으로_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(plannerService.findPlannerOrderByName(anyLong())).willReturn(플래너_이름순_조회_응답());

        // when, then
        mockMvc.perform(get("/api/planner/name")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/find/name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("tripScheduleResponses").description("여행 일정 리스트 : 이름순조회"),
                                fieldWithPath("tripScheduleResponses[].scheduleId").description("여행 일정 고유 ID 값"),
                                fieldWithPath("tripScheduleResponses[].scheduleName").description("여행 일정 이름"),
                                fieldWithPath("tripScheduleResponses[].startTime").description("여행 일정 시작날짜"),
                                fieldWithPath("tripScheduleResponses[].endTime").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isOk());
    }

    @DisplayName("플래너의 여행지를 날짜순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 플래너의_여행지를_날짜순으로_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(plannerService.findPlannerOrderByDateAsc(anyLong())).willReturn(플래너_날짜순_조회_응답());

        // when, then
        mockMvc.perform(get("/api/planner/date")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/find/date",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("tripScheduleResponses").description("여행 일정 리스트 : 날짜순조회"),
                                fieldWithPath("tripScheduleResponses[].scheduleId").description("여행 일정 고유 ID 값"),
                                fieldWithPath("tripScheduleResponses[].scheduleName").description("여행 일정 이름"),
                                fieldWithPath("tripScheduleResponses[].startTime").description("여행 일정 시작날짜"),
                                fieldWithPath("tripScheduleResponses[].endTime").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isOk());
    }

    @DisplayName("존재하지 않는 멤버의 플래너를 조회하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_멤버의_플래너를_조회하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistMemberException("존재하지 않는 회원입니다."))
                .when(plannerService).findPlannerOrderByRecent(anyLong());

        // when, then
        mockMvc.perform(get("/api/planner/recent")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/find/recent/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("여행 일정을 수정하고 상태코드 204를 리턴한다.")
    @Test
    void 여행_일정을_수정하고_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(plannerService).updateTripSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(put("/api/planner/schedule")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(여행_일정_수정_요청())))
                .andDo(print())
                .andDo(document("planner/schedule/update/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("scheduleId").description("여행 일정 고유 ID 값"),
                                fieldWithPath("scheduleName").description("여행 일정 이름"),
                                fieldWithPath("startDate").description("일정 시작날짜"),
                                fieldWithPath("endDate").description("일정 종료날짜")
                        )))
                .andExpect(status().isNoContent());
    }

    @DisplayName("여행 일정의 이름이 변경된 경우 중복 이름을 체크하고, 중복이 발생했다면 상태코드 400을 리턴한다.")
    @Test
    void 여행_일정의_이름이_변경된_경우_중복_이름을_체크하고_중복이_발생했다면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new AlreadyExistTripScheduleException("동일한 이름의 여행 일정이 이미 존재합니다."))
                .when(plannerService).updateTripSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(put("/api/planner/schedule")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(여행_일정_수정_요청())))
                .andDo(document("planner/schedule/update/fail/duplicateName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("scheduleId").description("여행 일정 고유 ID 값"),
                                fieldWithPath("scheduleName").description("여행 일정 이름"),
                                fieldWithPath("startDate").description("일정 시작날짜"),
                                fieldWithPath("endDate").description("일정 종료날짜")
                        )))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("존재하지 않는 여행 일정을 수정하면 상태코드 404을 리턴한다.")
    @Test
    void 존재하지_않는_여행_일정을_수정하면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripScheduleException("존재하지 않는 여행 일정입니다."))
                .when(plannerService).updateTripSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(put("/api/planner/schedule")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(여행_일정_수정_요청())))
                .andDo(print())
                .andDo(document("planner/schedule/update/fail/noExistTripSchedule",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("scheduleId").description("여행 일정 고유 ID 값"),
                                fieldWithPath("scheduleName").description("여행 일정 이름"),
                                fieldWithPath("startDate").description("일정 시작날짜"),
                                fieldWithPath("endDate").description("일정 종료날짜")
                        )))
                .andExpect(status().isNotFound());
    }

    @DisplayName("여행 일정을 삭제하고 상태코드 204를 리턴한다.")
    @Test
    void 여행_일정을_삭제하고_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(plannerService).removeTripSchedule(anyLong());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/planner/schedule/{scheduleId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/schedule/delete/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                        ))
                .andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 여행 일정을 삭제하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_여행_일정을_삭제하려고_하면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripScheduleException("존재하지 않는 여행 일정입니다."))
                .when(plannerService).removeTripSchedule(anyLong());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/planner/schedule/{scheduleId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/schedule/delete/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("주어진 범위에 해당하는 플래너 여행 일정들을 내림차순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 주어진_범위에_해당하는_플래너_여행_일정들을_내림차순으로_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(plannerService.findPlannerOrderByDateAndRange(anyLong(), any())).willReturn(플래너_날짜순_범위_조회_응답());

        // when, then
        mockMvc.perform(get("/api/planner/range")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(플래너_날짜순_범위_조회_요청())))
                .andDo(print())
                .andDo(document("planner/find/range/date/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("startDate").description("일정 시작날짜"),
                                fieldWithPath("endDate").description("일정 종료날짜")
                        ),
                        responseFields(
                                fieldWithPath("tripScheduleResponses").description("여행 일정 리스트 : 범위내의 날짜순조회"),
                                fieldWithPath("tripScheduleResponses[].scheduleId").description("여행 일정 고유 ID 값"),
                                fieldWithPath("tripScheduleResponses[].scheduleName").description("여행 일정 이름"),
                                fieldWithPath("tripScheduleResponses[].startTime").description("여행 일정 시작날짜"),
                                fieldWithPath("tripScheduleResponses[].endTime").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isOk());
    }

    @DisplayName("존재하지 않는 멤버의 주어진 범위에 해당하는 플래너 여행 일정들을 조회하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_멤버의_주어진_범위에_해당하는_플래너_여행_일정들을_조회하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistMemberException("존재하지 않는 회원입니다."))
                .when(plannerService).findPlannerOrderByDateAndRange(anyLong(), any());

        // when, then
        mockMvc.perform(get("/api/planner/range")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(플래너_날짜순_범위_조회_요청())))
                .andDo(print())
                .andDo(document("planner/find/range/date/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("startDate").description("일정 시작날짜"),
                                fieldWithPath("endDate").description("일정 종료날짜")
                        )
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("이번달의 여행 일정을 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 이번달의_여행_일정을_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);

        // when, then
        mockMvc.perform(get("/api/planner/month")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
