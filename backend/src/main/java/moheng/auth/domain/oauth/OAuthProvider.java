package moheng.auth.domain.oauth;

public interface OAuthProvider {
    OAuthClient getOauthClient(final String providerName);
    OAuthUriProvider getOAuthUriProvider(final String providerName);
}
