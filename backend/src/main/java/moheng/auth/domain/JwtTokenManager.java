package moheng.auth.domain;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenManager implements TokenManager {
    @Override
    public MemberToken createMemberToken(long memberId) {
        return null;
    }

    @Override
    public String generateRenewalAccessToken(String refreshToken) {
        return "";
    }

    @Override
    public String getMemberId(String accessToken) {
        return "";
    }

    @Override
    public void removeRefreshToken(String refreshToken) {

    }
}
