package moheng.config;

import moheng.auth.domain.oauth.OAuthProvider;
import moheng.config.stub.StubKakaoOAuthClientProvider;
import moheng.config.stub.StubKeywordFilterModelClient;
import moheng.config.stub.StubSimilarTripModelClient;
import moheng.keyword.service.KeywordFilterModelClient;
import moheng.trip.domain.ExternalSimilarTripModelClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public OAuthProvider oAuthProvider() {
        return new StubKakaoOAuthClientProvider();
    }

    @Bean
    public KeywordFilterModelClient keywordFilterModelClient() {
        return new StubKeywordFilterModelClient();
    }
}
