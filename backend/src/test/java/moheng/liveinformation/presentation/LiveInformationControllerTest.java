package moheng.liveinformation.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static moheng.fixture.MemberFixtures.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import moheng.config.slice.ControllerTestConfig;
import moheng.keyword.exception.NoExistKeywordException;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.dto.FindAllLiveInformationResponse;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import moheng.liveinformation.exception.LiveInfoNameException;
import moheng.liveinformation.exception.NoExistLiveInformationException;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import java.util.List;

public class LiveInformationControllerTest extends ControllerTestConfig {

    @DisplayName("모든 생활정보를 찾고 상태코드 200을 리턴한다.")
    @Test
    void 모든_생활정보를_찾고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(liveInformationService.findAllLiveInformation())
                .willReturn(new FindAllLiveInformationResponse(
                        List.of(new LiveInformation(1L, "생활정보1"),
                                new LiveInformation(2L, "생활정보2"),
                                new LiveInformation(3L, "생활정보3")
                        )
                ));

        // when, then
        mockMvc.perform(get("/api/live/info/all")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andDo(document("live/info/findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("liveInformationResponses").description("모든 생활정보의 id 및 이름"),
                                fieldWithPath("liveInformationResponses[].id").description("생활정보 ID"),
                                fieldWithPath("liveInformationResponses[].name").description("생활정보 이름")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("생활정보를 생성하면 상태코드 204를 리턴한다.")
    @Test
    void 생활정보를_생성하면_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(liveInformationService).createLiveInformation(any());

        // when, then
        mockMvc.perform(post("/api/live/info")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(생활정보로_회원가입_요청()))
        ).andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("생성할 생활정보 이름의 길이가 유효범위를 벗어나면 상태코드 400을 리턴한다.")
    @Test
    void 생성할_생활정보_이름의_길이가_유효범위를_벗어나면_상태코드_400을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new LiveInfoNameException("생활정보 이름의 길이는 최소 1자 이상, 최대 100자 이하만 허용됩니다."))
                .when(liveInformationService).createLiveInformation(any());

        // when, then
        mockMvc.perform(post("/api/live/info")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LiveInformationCreateRequest("")))
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("여행지의 생활정보를 생성하고 상태코드 204를 리턴한다.")
    @Test
    void 여행지의_생활정보를_생성하고_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doNothing().when(liveInformationService).createTripLiveInformation(anyLong(), anyLong());

        // when, then
        mockMvc.perform(post("/api/live/info/trip/{tripId}/{liveInfoId}", 1L, 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("존재하지 않는 여행지의 여행 생활정보를 생성하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_여행지의_여행_생활정보를_생성하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistTripException("랜덤 키워드를 찾을 수 없습니다."))
                .when(liveInformationService).createTripLiveInformation(anyLong(), anyLong());

        // when, then
        mockMvc.perform(post("/api/live/info/trip/{tripId}/{liveInfoId}", 1L, 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("존재하지 않는 생활정보의 여행 생활정보를 생성하려고 하면 상태코드 404를 리턴한다.")
    @Test
    void 존재하지_않는_생활정보의_여행_생활정보를_생성하려고_하면_상태코드_404를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        doThrow(new NoExistLiveInformationException("랜덤 키워드를 찾을 수 없습니다."))
                .when(liveInformationService).createTripLiveInformation(anyLong(), anyLong());

        // when, then
        mockMvc.perform(post("/api/live/info/trip/{tripId}/{liveInfoId}", 1L, 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
