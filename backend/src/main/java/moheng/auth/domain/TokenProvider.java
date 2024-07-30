package moheng.auth.domain;

public interface TokenProvider {
    MemberToken createMemberToken(final long memberId);
    String generateRenewalAccessToken(final String refreshToken);
    String getMemberId(final String accessToken);
    void removeRefreshToken(final String refreshToken);
}
