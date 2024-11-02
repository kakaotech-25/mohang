package moheng.auth.domain.oauth;

public interface OAuthMember {
    String getSocialLoginId();
    String getEmail();
    String getProfileImageUrl();
}
