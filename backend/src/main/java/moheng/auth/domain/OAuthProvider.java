package moheng.auth.domain;

public interface OAuthProvider {
    OAuthClient getOauthClient(final String providerName);
    OAuthUriProvider getOAuthUriProvider(final String providerName);
}
