package moheng.liveinformation.presentation;

import moheng.config.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static moheng.fixture.MemberFixtures.비어있는_생활정보로_회원가입_요청;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class LiveInformationControllerTest extends ControllerTestConfig {

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
        ).andDo(print());
    }
}
