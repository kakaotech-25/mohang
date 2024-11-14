package moheng.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public class LogoutRequest {
    @NotNull(message = "리프레시 토큰은 공백일 수 없습니다.")
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
