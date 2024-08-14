package moheng.config.stub;

import moheng.auth.infrastructure.KakaoOAuthMember;
import moheng.auth.domain.oauth.OAuthClient;
import moheng.auth.domain.oauth.OAuthMember;


public class KakaoStubOAuthClient implements OAuthClient {
    private final String PROVIDER_NAME = "kakao";

    @Override
    public OAuthMember getOAuthMember(String code) {
        return new KakaoOAuthMember("stub@kakao.com", "kakao_login_id");
    }

    @Override
    public boolean isSame(String oAuthProviderName) {
        return PROVIDER_NAME.equals(oAuthProviderName);
    }
}
