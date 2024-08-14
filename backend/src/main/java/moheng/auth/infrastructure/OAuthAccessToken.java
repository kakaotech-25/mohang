package moheng.auth.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import moheng.global.annotation.Generated;

public class OAuthAccessToken {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("token_in")
    private int tokenIn;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @Generated
    public String getAccessToken() {
        return accessToken;
    }
}
