package moheng.auth.domain;

public interface RefreshTokenRepository {
    void save(final long memberId, final String refreshToken);
    boolean existsById(final long memberId);
    String findById(final long memberId);
}
