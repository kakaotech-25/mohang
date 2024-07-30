package moheng.auth.domain.oauth;


public interface OAuthClient {
    OAuthMember getOAuthMember(final String code);
    boolean isSame(String oAuthProviderName);
}
