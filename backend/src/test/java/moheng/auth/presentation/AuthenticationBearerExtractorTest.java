package moheng.auth.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

import jakarta.servlet.http.HttpServletRequest;
import moheng.auth.exception.EmptyBearerHeaderException;
import moheng.auth.exception.InvalidTokenFormatException;
import moheng.auth.presentation.authentication.AuthenticationBearerExtractor;
import moheng.config.TestConfig;
import moheng.config.slice.ControllerTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AuthenticationBearerExtractorTest {
    @Autowired
    private AuthenticationBearerExtractor authenticationBearerExtractor;

    @MockBean
    protected HttpServletRequest httpServletRequest;

    @DisplayName("헤더의 Authorization 값이 비어있으면 예외가 발생한다.")
    @Test
    void 헤더의_Authorization_값이_비어있으면_예외가_발생한다() {
        given(httpServletRequest.getHeader("Authorization")).willReturn(null);

        assertThatThrownBy(() -> authenticationBearerExtractor.extract(httpServletRequest))
                .isInstanceOf(EmptyBearerHeaderException.class);
    }

    @DisplayName("헤더의 Authorization 값이 유효하지 않다면 예외가 발생한다.")
    @Test
    void 헤더의_Authorization_값이_유효하지_않다면_예외가_발생한다() {
        given(httpServletRequest.getHeader("Authorization")).willReturn("invalid token");

        assertThatThrownBy(() -> authenticationBearerExtractor.extract(httpServletRequest))
                .isInstanceOf(InvalidTokenFormatException.class);
    }
}
