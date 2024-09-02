package moheng.planner.presentation;

import static moheng.fixture.MemberFixtures.하온_기존;
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
import moheng.member.exception.ShortContentidsSizeException;
import moheng.planner.domain.TripSchedule;
import moheng.planner.dto.FindPlannerOrderByDateResponse;
import moheng.planner.dto.FindTripOnSchedule;
import moheng.planner.dto.FindTripsOnSchedule;
import moheng.planner.dto.UpdateTripOrdersRequest;
import moheng.planner.exception.*;
import moheng.trip.domain.Trip;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.time.LocalDate;
import java.util.List;

public class TripScheduleControllerTest extends ControllerTestConfig {

    @DisplayName("여행 일정을 생성하고 상태코드 204를 리턴한다.")
    @Test
    void 여행_일정을_생성하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(tripScheduleService).createTripSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/schedule")
                                .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(여행_일정_생성_요청()))
                ).andDo(print())
                .andDo(document("planner/schedule/create/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("scheduleName").description("생성할 여행 일정 이름"),
                                fieldWithPath("startDate").description("여행 일정 시작날짜"),
                                fieldWithPath("endDate").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isNoContent());
    }

    @DisplayName("동일한 이름의 여행 일정이 이미 존재하면 상태코드 400을 리턴한다.")
    @Test
    void 동일한_이름의_여행_일정이_이미_존재하면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new AlreadyExistTripScheduleException("동일한 이름의 여행 일정이 이미 존재합니다."))
                .when(tripScheduleService).createTripSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/schedule")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(여행_일정_생성_요청()))
                ).andDo(print())
                .andDo(document("planner/schedule/create/fail/alreadyExistTripSchedule",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("scheduleName").description("생성할 여행 일정 이름"),
                                fieldWithPath("startDate").description("여행 일정 시작날짜"),
                                fieldWithPath("endDate").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("생성할 여행 일정 이름 길이가 유효범위를 벗어났다면 상태코드 400을 리턴한다.")
    @Test
    void 생성할_여행_일정_이름_길이가_유효범위를_벗어났다면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new InvalidTripScheduleNameException("일정 이름은 2자 이상 1자 100자 이하여야 합니다."))
                .when(tripScheduleService).createTripSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/schedule")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유효하지_않은_이름으로_여행_일정_생성_요청()))
                ).andDo(print())
                .andDo(document("planner/schedule/create/fail/invalidName",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("scheduleName").description("생성할 여행 일정 이름"),
                                fieldWithPath("startDate").description("여행 일정 시작날짜"),
                                fieldWithPath("endDate").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("생성할 여행 일정의 시작날짜가 종료날짜보다 늦다면 상태코드 400을 리턴한다.")
    @Test
    void 생성할_여행_일정의_시작날짜가_종료날짜보다_늦다면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new InvalidTripScheduleDateException("플래너 일정의 시작날짜가 종료날짜보다 늦을 수 없습니다."))
                .when(tripScheduleService).createTripSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(post("/api/schedule")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유효하지_않은_날짜로_여행_일정_생성_요청()))
                ).andDo(print())
                .andDo(document("planner/schedule/create/fail/invalidDate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("scheduleName").description("생성할 여행 일정 이름"),
                                fieldWithPath("startDate").description("여행 일정 시작날짜"),
                                fieldWithPath("endDate").description("여행 일정 종료날짜")
                        )))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("현재 여행지를 플래너 일정에 담고 상태코드 204를 리턴한다.")
    @Test
    void 현재_여행지를_플래너_일정에_담고_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(tripScheduleService).addCurrentTripOnPlannerSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/schedule/trip/{tripId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andDo(document("planner/schedule/trip/add/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("tripId").description("플래너에 담을 현재 여행지의 고유 ID 값"),
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값")
                        )
                ))
                .andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 여행지를 일정에 추가하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_여행지를_일정에_추가하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripException("존재하지 않는 여행지입니다."))
                .when(tripScheduleService).addCurrentTripOnPlannerSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/schedule/trip/{tripId}/{scheduleId}", 1L, 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andDo(document("planner/schedule/trip/add/fail/noExistTrip",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("tripId").description("플래너에 담을 현재 여행지의 고유 ID 값"),
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값")
                        )
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("존재하지 않는 일정에 여행지를 추가하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_일정에_여행지를_추가하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripScheduleException("존재하지 않는 여행 일정입니다."))
                .when(tripScheduleService).addCurrentTripOnPlannerSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/schedule/trip/{tripId}", 1L, 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andDo(document("planner/schedule/trip/add/fail/noExistSchedule",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("tripId").description("플래너에 담을 현재 여행지의 고유 ID 값"),
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값")
                        )
                ))
                .andExpect(status().isNotFound());
    }

    @DisplayName("세부 일정 정보를 여행지 리스트와 함께 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 세부_일정_정보를_여행지_리스트와_함께_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(tripScheduleService.findTripsOnSchedule(anyLong())).willReturn(
                new FindTripsOnSchedule(
                        new TripSchedule("제주도 여행", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), 하온_기존()),
                        List.of(
                                new Trip("롯데월드1", "서울1 어딘가", 1L, "설명1", "https://lotte.png", 126.3307690830, 36.5309210243),
                                new Trip("롯데월드2", "서울2 어딘가", 2L, "설명2", "https://lotte.png", 226.3307690830, 46.5309210243),
                                new Trip("롯데월드3", "서울3 어딘가", 3L, "설명3", "https://lotte.png", 326.3307690830, 56.5309210243)
                        )
                )
        );

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/schedule/trips/{scheduleId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/schedule/find/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값")
                        ),
                        responseFields(
                                fieldWithPath("tripScheduleResponse").description("세부 일정"),
                                fieldWithPath("tripScheduleResponse.scheduleId").description("여행 일정 고유 ID 값"),
                                fieldWithPath("tripScheduleResponse.scheduleName").description("여행 일정 이름"),
                                fieldWithPath("tripScheduleResponse.startTime").description("여행 일정 시작날짜"),
                                fieldWithPath("tripScheduleResponse.endTime").description("여행 일정 종료날짜"),
                                fieldWithPath("findTripsOnSchedules").description("세부 일정내의 여행지 리스트"),
                                fieldWithPath("findTripsOnSchedules[].tripId").description("여행지 고유 ID 값"),
                                fieldWithPath("findTripsOnSchedules[].placeName").description("여행지 장소명"),
                                fieldWithPath("findTripsOnSchedules[].coordinateX").description("여행지 X축 좌표값"),
                                fieldWithPath("findTripsOnSchedules[].coordinateY").description("여행지 Y축 좌표값")
                        )))
                .andExpect(status().isOk());
    }

    @DisplayName("존재하지 않는 일정 정보를 여행지 리스트와 함께 조회하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_일정_정보를_여행지_리스트와_함께_조회하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripScheduleException("존재하지 않는 여행 일정입니다."))
                .when(tripScheduleService).findTripsOnSchedule(anyLong());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/schedule/trips/{scheduleId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/schedule/find/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값")
                        )))
                .andExpect(status().isNotFound());
    }

    @DisplayName("세부 일정내의 여행지 리스트 순서를 수정하고 상태코드 204를 리턴한다.")
    @Test
    void 세부_일정내의_여행지_리스트_순서를_수정하고_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(tripScheduleService).updateTripOrdersOnSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/schedule/trips/orders/{scheduleId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateTripOrdersRequest(List.of(1L, 2L, 3L)))))
                .andDo(print())
                .andDo(document("planner/schedule/order/update/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값")
                        ),
                        requestFields(
                                fieldWithPath("tripIds").description("새롭게 수정할 여행지 리스트 ID 값 리스트. (순서가 변경되지 않은 내용들도 포함하여 일정내의 ID 리스트 값 전체를 모두 전송해주세요)")
                        )
                )).andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 여행지로 세부 일정내의 여행지 리스트 순서를 수정하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_여행지로_세부_일정내의_여행지_리스트_순서를_수정하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripScheduleException("존재하지 않는 여행 일정입니다."))
                .when(tripScheduleService).updateTripOrdersOnSchedule(anyLong(), any());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/schedule/trips/orders/{scheduleId}", 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateTripOrdersRequest(List.of(1L, 2L, 3L)))))
                .andDo(print())
                .andDo(document("planner/schedule/order/update/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값")
                        ),
                        requestFields(
                                fieldWithPath("tripIds").description("새롭게 수정할 여행지 리스트 ID 값 리스트. (순서가 변경되지 않은 내용들도 포함하여 일정내의 ID 리스트 값 전체를 모두 전송해주세요)")
                        )
                )).andExpect(status().isNotFound());
    }

    @DisplayName("세부 일정내의 특정 여행지를 제거하고 상태코드 204를 리턴한다.")
    @Test
    void 세부_일정내의_특정_여행지를_제거하고_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(tripScheduleService).deleteTripOnSchedule(anyLong(), anyLong());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/schedule/{scheduleId}/{tripId}", 1L, 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/schedule/trip/delete/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값"),
                                parameterWithName("tripId").description("여행지 고유 ID 값")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                )).andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 일정 여행지를 삭제하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_일정_여행지를_삭제하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripScheduleRegistryException("존재하지 않는 일정 여행지입니다."))
                .when(tripScheduleService).deleteTripOnSchedule(anyLong(), anyLong());

        // when, then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/schedule/{scheduleId}/{tripId}", 1L, 1L)
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("planner/schedule/trip/delete/fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("scheduleId").description("여행 일정 고유 ID 값"),
                                parameterWithName("tripId").description("여행지 고유 ID 값")
                        ),
                        requestHeaders(
                                headerWithName("Authorization").description("엑세스 토큰")
                        )
                )).andExpect(status().isNotFound());
    }
}
