package moheng.applicationrunner.warmup;

import moheng.auth.application.AuthService;
import moheng.auth.dto.RenewalAccessTokenRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile({"dev", "prod"})
@Order(4)
@Component
public class AuthWarmUpRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AuthWarmUpRunner.class);
    private final AuthService authService;

    public AuthWarmUpRunner(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            authService.generateUri("URI");
            authService.generateTokenWithCode("CODE", "PROVIDER");
            authService.generateRenewalAccessToken(new RenewalAccessTokenRequest("TOKEN"));
        } catch (Exception e) {
            // log.info("Auth Warm Up 처리중입니다.");
        }
    }
}
