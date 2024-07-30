package moheng.auth.domain.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthMember {
    @JsonProperty("id")
    private String socialLoginId;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    private class KakaoAccount {

        @JsonProperty("email")
        private String email;
    }
    public OAuthMember(String email, String socialLoginId, String nickname, String imageurl) {
        this.socialLoginId = socialLoginId;
        this.kakaoAccount = new KakaoAccount();
        this.kakaoAccount.email = email;
    }

    public String getSocialLoginId() {
        return socialLoginId;
    }


    public String getEmail() {
        return kakaoAccount.email;
    }
}

