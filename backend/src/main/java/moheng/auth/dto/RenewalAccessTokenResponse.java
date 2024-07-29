package moheng.auth.dto;

public class RenewalAccessTokenResponse {
    private String accessToken;

    private RenewalAccessTokenResponse() {
    }

    public RenewalAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
