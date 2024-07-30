package moheng.auth.domain.token;

public interface RefreshTokenRepository {
    void save(final long memberId, final String refreshToken);
    boolean existsById(final long memberId);
    String findById(final long memberId);
    long deleteById(final long memberId);
    void deleteAll();
}