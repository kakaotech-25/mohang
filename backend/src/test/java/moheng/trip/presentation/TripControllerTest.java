package moheng.trip.presentation;

import static org.mockito.BDDMockito.given;
import static moheng.fixture.MemberFixtures.*;
import static moheng.fixture.TripFixture.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import moheng.config.ControllerTestConfig;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsOrderByVisitedCountDescResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

public class TripControllerTest extends ControllerTestConfig {

    @DisplayName("여행지를 생성하면 상태코드 204를 리턴한다.")
    @Test
    void 여행지를_생성하면_상태코드_204를_리턴한다() throws Exception {
        // given, when, then
        mockMvc.perform(post("/trip")
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
                .willReturn(new FindTripsOrderByVisitedCountDescResponse(
                        List.of(new Trip("여행지1", "장소명1", 1L, "여행지1 설명", "여행지1 이미지 경로"),
                                new Trip("여행지2", "장소명2", 2L, "여행지2 설명", "여행지2 이미지 경로"),
                                new Trip("여행지3", "장소명3", 3L, "여행지3 설명", "여행지3 이미지 경로")
                )
        ));

        // when, then
        mockMvc.perform(get("/trip/find/interested")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andDo(document("trip/find/interested",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("findTripResponses").description("여행지 리스트"),
                                fieldWithPath("findTripResponses[].name").description("세부 여행지 이름"),
                                fieldWithPath("findTripResponses[].placeName").description("세부 여행지 장소명"),
                                fieldWithPath("findTripResponses[].contentId").description("세부 여행지 contentId"),
                                fieldWithPath("findTripResponses[].tripImageUrl").description("세부 여행지 이미지 경로")
                        )
                ))
                .andExpect(status().isOk());
    }
}
