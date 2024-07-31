package moheng.auth.dto;

public class AccessTokenResponse {

    private String accessToken;

    private AccessTokenResponse() {
    }

    public AccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}