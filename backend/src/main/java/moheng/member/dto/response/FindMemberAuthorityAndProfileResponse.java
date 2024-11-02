package moheng.member.dto.response;

import moheng.auth.domain.oauth.Authority;

public class FindMemberAuthorityAndProfileResponse {
    private Authority authority;
    private String profileImageUrl;

    private FindMemberAuthorityAndProfileResponse() {
    }

    public FindMemberAuthorityAndProfileResponse(final Authority authority, final String profileImageUrl) {
        this.authority = authority;
        this.profileImageUrl = profileImageUrl;
    }

    public Authority getAuthority() {
        return authority;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
