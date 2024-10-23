package moheng.config.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.auth.application.AuthService;
import moheng.auth.domain.token.JwtTokenGenerator;
import moheng.auth.presentation.AuthController;
import moheng.auth.presentation.authentication.AuthenticationBearerExtractor;
import moheng.config.TestConfig;
import moheng.keyword.application.KeywordService;
import moheng.keyword.presentation.KeywordController;
import moheng.liveinformation.application.LiveInformationService;
import moheng.liveinformation.application.MemberLiveInformationService;
import moheng.liveinformation.presentation.LiveInformationController;
import moheng.liveinformation.presentation.MemberLiveInformationController;
import moheng.member.application.MemberService;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.presentation.MemberController;
import moheng.planner.application.PlannerService;
import moheng.planner.application.TripScheduleService;
import moheng.planner.presentation.PlannerController;
import moheng.planner.presentation.TripScheduleController;
import moheng.recommendtrip.application.RecommendTripService;
import moheng.recommendtrip.domain.tripfilterstrategy.*;
import moheng.recommendtrip.presentation.RecommendTripController;
import moheng.trip.application.TripService;
import moheng.trip.presentation.TripController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest({
        AuthController.class,
        KeywordController.class,
        LiveInformationController.class,
        MemberLiveInformationController.class,
        MemberController.class,
        RecommendTripController.class,
        TripController.class,
        TripScheduleController.class,
        PlannerController.class,
})
@Import(TestConfig.class)
@ActiveProfiles("test")
public abstract class ControllerTestConfig {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected JwtTokenGenerator jwtTokenProvider;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected AuthenticationBearerExtractor authenticationBearerExtractor;

    @MockBean
    protected LiveInformationService liveInformationService;

    @MockBean
    protected TripService tripService;

    @MockBean
    protected MemberLiveInformationService memberLiveInformationService;

    @MockBean
    protected MemberRepository memberRepository;

    @MockBean
    protected KeywordService keywordService;

    @MockBean
    protected RecommendTripService recommendTripService;

    @MockBean
    protected TripScheduleService tripScheduleService;

    @MockBean
    protected PlannerService plannerService;

    @MockBean
    protected TripFilterStrategyProvider tripFilterStrategyProvider;

    @MockBean
    protected TripFilterStrategy tripFilterStrategy;

    @MockBean
    protected TripsWithKeywordProvider tripsWithKeywordProvider;
}
