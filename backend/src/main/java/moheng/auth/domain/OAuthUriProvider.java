package moheng.auth.domain;


public interface OAuthUriProvider {
    String generateUri();
    boolean isSame(String provider);
}
