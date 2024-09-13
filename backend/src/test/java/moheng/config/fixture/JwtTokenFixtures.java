package moheng.config.fixture;

public class JwtTokenFixtures {
    public static final String SECRET_KEY = "secret_secret_secret_secret_secret_secret_secret_";
    public static final int ACCESS_TOKEN_EXPIRE_TIME = 3600;
    public static final int REFRESH_TOKEN_EXPIRE_TIME = 3600;
    public static final int EXPIRED_TOKEN_TIME = 0;
    public static final String PAYLOAD = "payload";
    public static final String INVALID_PAYLOAD = "invalid payload";
    public static final String INVALID_REFRESH_TOKEN = "expired.payload.payload";
}
