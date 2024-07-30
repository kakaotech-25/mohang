package moheng.auth.domain.token;

public interface TokenManager {
    MemberToken createMemberToken(final long memberId);
    String generateRenewalAccessToken(final String refreshToken);
    Long getMemberId(final String accessToken);
    void removeRefreshToken(final String refreshToken);
}