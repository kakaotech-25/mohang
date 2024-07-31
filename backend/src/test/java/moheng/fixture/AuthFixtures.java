package moheng.fixture;

import moheng.auth.domain.token.MemberToken;
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
}
