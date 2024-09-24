package moheng.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.auth.domain.oauth.OAuthClientProvider;
import moheng.config.slice.ServiceTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OAuthClientProviderTest extends ServiceTestConfig {

    @Autowired
    private OAuthClientProvider oAuthClientProvider;

    @DisplayName("소셜 로그인 제공처를 찾는다.")
    @Test
    public void 소셜_로그인_제공처를_찾는다() {
        assertDoesNotThrow(() -> oAuthClientProvider.getOauthClient("KAKAO"));
    }

    @DisplayName("소셜 로그인 인가 URI 제공처를 찾는다.")
    @Test
    void 소셜_로그인_인가_URI_제공처를_찾는다() {
        assertDoesNotThrow(() -> oAuthClientProvider.getOAuthUriProvider("KAKAO"));
    }
}
