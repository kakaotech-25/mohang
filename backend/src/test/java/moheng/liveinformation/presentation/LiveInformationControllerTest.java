package moheng.liveinformation.presentation;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static moheng.fixture.MemberFixtures.비어있는_생활정보로_회원가입_요청;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import moheng.config.ControllerTestConfig;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.dto.FindAllLiveInformationResponse;
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
        mockMvc.perform(get("/live/info")
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
        mockMvc.perform(post("/live/info")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(비어있는_생활정보로_회원가입_요청()))
        ).andDo(print())
                .andExpect(status().isNoContent());
    }
}
