package moheng.planner.presentation;

import moheng.config.slice.ControllerTestConfig;
import moheng.planner.dto.CreateTripScheduleRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static moheng.fixture.MemberFixtures.비어있는_생활정보로_회원가입_요청;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

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
                .content(objectMapper.writeValueAsString(new CreateTripScheduleRequest("제주도 여행", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10))))
                ).andDo(print())
                .andExpect(status().isNoContent());
    }
}
