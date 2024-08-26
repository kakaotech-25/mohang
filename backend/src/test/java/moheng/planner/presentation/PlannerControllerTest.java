package moheng.planner.presentation;

import static moheng.fixture.MemberFixtures.*;
import static moheng.fixture.TripScheduleFixtures.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import moheng.config.slice.ControllerTestConfig;
import moheng.planner.domain.TripSchedule;
import moheng.planner.dto.FindPLannerOrderByNameResponse;
import moheng.planner.dto.FindPlannerOrderByDateResponse;
import moheng.planner.dto.FindPlannerOrderByRecentResponse;
import moheng.planner.dto.UpdateTripScheduleRequest;
import moheng.planner.exception.AlreadyExistTripScheduleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

public class PlannerControllerTest extends ControllerTestConfig {

    @DisplayName("플래너의 여행지를 최신순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 플래너의_여행지를_최신순으로_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(plannerService.findPlannerOrderByRecent(anyLong())).willReturn(
                new FindPlannerOrderByRecentResponse(List.of(
                        new TripSchedule("제주도 여행", LocalDate.of(2020, 3, 10), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("카카오 여행", LocalDate.of(2024, 2, 15), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("네이버 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("우주 여행", LocalDate.of(2023, 1, 1), LocalDate.of(2030, 1, 10), 하온_기존())
                ))
        );

        // when, then
        mockMvc.perform(get("/api/planner/recent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/find/recent",
                        preprocessRequest(),
                        responseFields(
                                fieldWithPath("tripScheduleResponses").description("여행 일정 리스트 : 최신순조회"),
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
        given(plannerService.findPlannerOrderByName(anyLong())).willReturn(
                new FindPLannerOrderByNameResponse(List.of(
                        new TripSchedule("가 여행", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("나 여행", LocalDate.of(2023, 2, 15), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("다 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("라 여행", LocalDate.of(2021, 1, 1), LocalDate.of(2030, 1, 10), 하온_기존())
                ))
        );

        // when, then
        mockMvc.perform(get("/api/planner/name")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/find/name",
                        preprocessRequest(),
                        responseFields(
                                fieldWithPath("tripScheduleResponses").description("여행 일정 리스트 : 이름순조회"),
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
        given(plannerService.findPlannerOrderByDateAsc(anyLong())).willReturn(
                new FindPlannerOrderByDateResponse(List.of(
                        new TripSchedule("제주도 여행", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("카카오 여행", LocalDate.of(2023, 2, 15), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("네이버 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("우주 여행", LocalDate.of(2021, 1, 1), LocalDate.of(2030, 1, 10), 하온_기존())
                ))
        );

        // when, then
        mockMvc.perform(get("/api/planner/date")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/find/name",
                        preprocessRequest(),
                        responseFields(
                                fieldWithPath("tripScheduleResponses").description("여행 일정 리스트 : 날짜순조회"),
                                fieldWithPath("tripScheduleResponses[].scheduleName").description("여행 일정 이름"),
                                fieldWithPath("tripScheduleResponses[].startTime").description("여행 일정 시작날짜"),
                                fieldWithPath("tripScheduleResponses[].endTime").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isOk());
    }

    @DisplayName("여행 일정을 수정하고 상태코드 204를 리턴한다.")
    @Test
    void 여행_일정을_수정하고_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(plannerService).updateTripSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(put("/api/planner/schedule")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(여행_일정_수정_요청())))
                .andDo(print())
                .andDo(document("planner/schedule/update",
                        preprocessRequest(),
                        preprocessResponse(),
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
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(여행_일정_수정_요청())))
                .andDo(document("planner/schedule/update",
                        preprocessRequest(),
                        preprocessResponse(),
                        requestFields(
                                fieldWithPath("scheduleId").description("여행 일정 고유 ID 값"),
                                fieldWithPath("scheduleName").description("여행 일정 이름"),
                                fieldWithPath("startDate").description("일정 시작날짜"),
                                fieldWithPath("endDate").description("일정 종료날짜")
                        )))
                .andExpect(status().isBadRequest());
    }
}
