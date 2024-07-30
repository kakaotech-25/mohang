package moheng.auth.domain;

public interface OAuthProvider {
    OAuthClient getOauthClient(final String provider);
    OAuthUriProvider getOAuthUriProvider(final String provider);
}
