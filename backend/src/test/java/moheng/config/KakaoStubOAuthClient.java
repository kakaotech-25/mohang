package moheng.config;

import moheng.auth.domain.oauth.OAuthClient;
import moheng.auth.domain.oauth.OAuthMember;


public class KakaoStubOAuthClient implements OAuthClient {
    private final String PROVIDER_NAME = "kakao";

    @Override
    public OAuthMember getOAuthMember(String code) {
        return new OAuthMember("stub@kakao.com", "kakao_login_id", "kakao_nickname", "kakao_image_url");
    }

    @Override
    public boolean isSame(String oAuthProviderName) {
        return PROVIDER_NAME.equals(oAuthProviderName);
    }
}
