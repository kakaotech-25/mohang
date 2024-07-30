package moheng.config;

import moheng.auth.domain.*;

public class StubKakaoOAuthClientProvider implements OAuthProvider {
    @Override
    public OAuthClient getOauthClient(String providerName) {
        return new KakaoStubOAuthClient();
    }

    @Override
    public OAuthUriProvider getOAuthUriProvider(String providerName) {
        return new KakaoUriProvider("stub_redirect_url",
                "stub_client_id", "stub_client_secret");
    }
}
