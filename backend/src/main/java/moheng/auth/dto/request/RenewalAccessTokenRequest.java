package moheng.auth.dto.request;

public class RenewalAccessTokenRequest {
    private String refreshToken;

    private RenewalAccessTokenRequest() {
    }

    public RenewalAccessTokenRequest(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
