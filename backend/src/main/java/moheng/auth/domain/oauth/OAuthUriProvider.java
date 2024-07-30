package moheng.auth.domain.oauth;


public interface OAuthUriProvider {
    String generateUri();
    boolean isSame(String provider);
}
