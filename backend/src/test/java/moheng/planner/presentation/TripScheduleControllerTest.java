package moheng.planner.presentation;

import static moheng.fixture.TripScheduleFixtures.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import java.time.LocalDate;

public class TripScheduleControllerTest extends ControllerTestConfig {

    @DisplayName("여행 일정을 생성하고 상태코드 204를 리턴한다.")
    @Test
    void 여행_일정을_생성하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(tripScheduleService).createTripSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/schedule")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(여행_일정_생성_요청()))
                ).andDo(print())
                .andDo(document("planner/schedule/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("scheduleName").description("생성할 여행 일정 이름"),
                                fieldWithPath("startDate").description("여행 일정 시작날짜"),
                                fieldWithPath("endDate").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isNoContent());
    }
}
