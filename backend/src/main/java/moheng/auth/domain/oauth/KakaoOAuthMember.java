package moheng.auth.domain.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoOAuthMember implements OAuthMember {
    @JsonProperty("id")
    private String socialLoginId;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    private class KakaoAccount {

        @JsonProperty("email")
        private String email;
    }

    public KakaoOAuthMember(String email, String socialLoginId, String nickname, String imageurl) {
        this.socialLoginId = socialLoginId;
        this.kakaoAccount = new KakaoAccount();
        this.kakaoAccount.email = email;
    }

    @Override
    public String getSocialLoginId() {
        return socialLoginId;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }
}

