package moheng.config.fixture;

import jakarta.servlet.http.Cookie;
import moheng.auth.domain.token.MemberToken;
import moheng.auth.dto.RenewalAccessTokenRequest;
import moheng.auth.dto.RenewalAccessTokenResponse;
import moheng.auth.dto.TokenRequest;

public class AuthFixtures {
    public static final String AUHTORIZATION_CODE = "authorization code";
    public static final String KAKAO_PROVIDER_NAME = "KAKAO";
    public static final String GOOGLE_PROVIDER_NAME = "GOOGLE";

    public static TokenRequest 토큰_생성_요청() {
        return new TokenRequest("authorization-code");
    }

    public static MemberToken 토큰_응답() {
        return new MemberToken("access-token", "refresh-token");
    }

    public static Cookie 토큰_갱신_요청() {
        return new Cookie("refresh-token", "token-value");
    }

    public static Cookie 로그아웃_요청() {
        return new Cookie("refresh-token", "token-value");
    }

    public static RenewalAccessTokenResponse 토큰_갱신_응답() {
        return new RenewalAccessTokenResponse("access-token");
    }
}
