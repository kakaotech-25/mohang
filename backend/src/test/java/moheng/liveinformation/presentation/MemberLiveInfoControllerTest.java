package moheng.liveinformation.presentation;

import static moheng.fixture.MemberLiveInfoFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import moheng.config.slice.ControllerTestConfig;
import moheng.liveinformation.dto.FindMemberLiveInformationResponses;
import moheng.liveinformation.dto.LiveInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import java.util.List;

public class MemberLiveInfoControllerTest extends ControllerTestConfig {

    @DisplayName("멤버가 선택한 생활정보를 조회하고 상태코드 200을 리턴한다.")
    @Test
    void 멤버가_선택한_생활정보를_조회하고_상태코드_200을_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);
        given(memberLiveInformationService.findMemberSelectedLiveInformation(anyLong()))
                .willReturn(new FindMemberLiveInformationResponses( List.of(
                        new LiveInfoResponse(1L, "생활정보1", true),
                        new LiveInfoResponse(2L, "생활정보2", true),
                        new LiveInfoResponse(3L, "생활정보3", false)
                )));

        // when, then
        mockMvc.perform(get("/live/info/member")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("live/info/member/find",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("liveInfoResponses").description("멤버가 선택한, 선택하지 않은 모든 생활정보"),
                                fieldWithPath("liveInfoResponses[].liveInfoId").description("생활정보 ID"),
                                fieldWithPath("liveInfoResponses[].name").description("생활정보 이름"),
                                fieldWithPath("liveInfoResponses[].contain").description("멤버의 생활정보 보유(선택) 여부")
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("멤버의 생활정보를 수정하고 상태코드 204를 리턴한다.")
    @Test
    void 멤버의_생활정보를_수정하고_상태코드_204를_리턴한다() throws Exception {
        // given
        given(jwtTokenProvider.getMemberId(anyString())).willReturn(1L);

        // when, then
        mockMvc.perform(put("/live/info/member")
                        .header("Authorization", "Bearer aaaaaa.bbbbbb.cccccc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(멤버_생활정보_수정_요청()))
                )
                .andDo(print())
                .andDo(document("live/info/member/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("liveInfoIds").description("멤버가 새롭게 수정하고자 선택한 생활정보 id 리스트")
                        )
                ))
                .andExpect(status().isNoContent());
    }
}
