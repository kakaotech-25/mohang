package moheng.config;

import moheng.auth.domain.oauth.KakaoUriProvider;
import moheng.auth.domain.oauth.OAuthClient;
import moheng.auth.domain.oauth.OAuthProvider;
import moheng.auth.domain.oauth.OAuthUriProvider;
import moheng.member.domain.SocialType;

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

    @Override
    public SocialType getSocialType(String provider) {
        return SocialType.KAKAO;
    }
}
