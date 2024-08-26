package moheng.auth.domain.token;

import moheng.auth.exception.NoExistMemberTokenException;

public class RenewalToken {
    private final String accessToken;
    private final String refreshToken;

    public RenewalToken(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void validateHasSameRefreshToken(final String otherRefreshToken) {
        if (!refreshToken.equals(otherRefreshToken)) {
            throw new NoExistMemberTokenException("회원의 리프레시 토큰이 아닙니다.");
        }
    }
}
