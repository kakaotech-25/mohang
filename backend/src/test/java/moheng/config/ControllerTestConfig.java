package moheng.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import moheng.auth.application.AuthService;
import moheng.auth.domain.token.JwtTokenProvider;
import moheng.auth.domain.token.TokenManager;
import moheng.auth.presentation.authentication.AuthenticationArgumentResolver;
import moheng.auth.presentation.authentication.AuthenticationBearerExtractor;
import moheng.liveinformation.application.LiveInformationService;
import moheng.member.application.MemberService;
import moheng.trip.application.TripService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class ControllerTestConfig {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected MemberService memberService;

    @Autowired
    protected AuthenticationArgumentResolver authenticationArgumentResolver;

    @Autowired
    protected AuthenticationBearerExtractor authenticationBearerExtractor;

    @Mock
    protected HttpServletRequest httpServletRequest;

    @MockBean
    protected LiveInformationService liveInformationService;

    @MockBean
    protected TripService tripService;
}
