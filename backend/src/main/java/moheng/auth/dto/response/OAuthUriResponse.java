package moheng.auth.dto.response;

public class OAuthUriResponse {

    private String oAuthUri;

    private OAuthUriResponse() {
    }

    public OAuthUriResponse(final String oAuthUri) {
        this.oAuthUri = oAuthUri;
    }

    public String getoAuthUri() {
        return oAuthUri;
    }
}
