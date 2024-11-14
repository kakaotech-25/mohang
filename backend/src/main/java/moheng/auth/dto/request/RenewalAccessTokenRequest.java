package moheng.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public class RenewalAccessTokenRequest {
    @NotNull(message = "리프레시 토큰은 공백일 수 없습니다.")
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
