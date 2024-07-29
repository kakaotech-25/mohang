package moheng.auth.dto;

public class LogoutRequest {
    private String refreshToken;

    private LogoutRequest() {
    }

    public LogoutRequest(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
