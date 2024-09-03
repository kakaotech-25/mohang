package moheng.auth.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import moheng.auth.domain.oauth.OAuthMember;
import moheng.global.annotation.Generated;

public class KakaoOAuthMember implements OAuthMember {
    @JsonProperty("id")
    private String socialLoginId;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    private static class KakaoAccount {
        @JsonProperty("email")
        private String email;

        @JsonProperty("profile")
        private KakaoProfile kakaoProfile;

        // Constructor to initialize KakaoProfile
        public KakaoAccount() {
            this.kakaoProfile = new KakaoProfile();
        }
    }

    private static class KakaoProfile {
        @JsonProperty("profile_image_url")
        private String image;
    }

    public KakaoOAuthMember(final String email, final String socialLoginId, final String profileImage) {
        this.socialLoginId = socialLoginId;
        this.kakaoAccount = new KakaoAccount();
        this.kakaoAccount.email = email;
        this.kakaoAccount.kakaoProfile.image = profileImage;
    }

    @Override
    public String getSocialLoginId() {
        return socialLoginId;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public String getProfileImageUrl() {
        return kakaoAccount.kakaoProfile.image;
    }
}
