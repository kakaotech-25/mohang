package moheng.planner.presentation;

import static moheng.fixture.MemberFixtures.*;
import static moheng.fixture.TripScheduleFixtures.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import moheng.config.slice.ControllerTestConfig;
import moheng.planner.domain.TripSchedule;
import moheng.planner.dto.FindPlannerOrderByRecentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

public class PlannerControllerTest extends ControllerTestConfig {

    @DisplayName("플래너의 여행지를 최신순으로 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 플래너의_여행지를_최신순으로_조회학도_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(plannerService.findPlannerOrderByRecent(anyLong())).willReturn(
                new FindPlannerOrderByRecentResponse(List.of(
                        new TripSchedule("제주도 여행", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("카카오 여행", LocalDate.of(2023, 2, 15), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("네이버 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), 하온_기존()),
                        new TripSchedule("우주 여행", LocalDate.of(2021, 1, 1), LocalDate.of(2030, 1, 10), 하온_기존())
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
}
