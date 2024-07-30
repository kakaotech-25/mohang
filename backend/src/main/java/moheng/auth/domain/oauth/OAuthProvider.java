package moheng.auth.domain.oauth;

import moheng.member.domain.SocialType;

import java.util.Optional;

public interface OAuthProvider {
    OAuthClient getOauthClient(final String providerName);
    OAuthUriProvider getOAuthUriProvider(final String providerName);
    SocialType getSocialType(String provider);
}
