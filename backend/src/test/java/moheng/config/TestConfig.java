package moheng.config;

import moheng.auth.domain.oauth.OAuthProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public OAuthProvider oAuthProvider() {
        return new StubKakaoOAuthClientProvider();
    }
}
