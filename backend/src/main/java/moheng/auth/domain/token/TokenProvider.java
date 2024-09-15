package moheng.auth.domain.token;

public interface TokenProvider {
    String createAccessToken(final long memberId);
    String createRefreshToken(final long memberId);
    Long getMemberId(final String token);
    void validateToken(final String token);
    boolean isRefreshTokenExpired(final String token);
}
