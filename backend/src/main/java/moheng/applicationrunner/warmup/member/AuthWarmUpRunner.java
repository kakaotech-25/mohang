package moheng.applicationrunner.warmup.member;

import moheng.auth.application.AuthService;
import moheng.auth.dto.RenewalAccessTokenRequest;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AuthWarmUpRunner implements ApplicationRunner {
    private final AuthService authService;

    public AuthWarmUpRunner(final AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        authService.generateUri("URI");
        authService.generateTokenWithCode("CODE", "PROVIDER");
        authService.generateRenewalAccessToken(new RenewalAccessTokenRequest("TOKEN"));
    }
}
