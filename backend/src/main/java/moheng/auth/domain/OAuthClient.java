package moheng.auth.domain;


public interface OAuthClient {
    OAuthMember getOAuthMember(final String code);
    boolean isSame(String oAuthProviderName);
}
