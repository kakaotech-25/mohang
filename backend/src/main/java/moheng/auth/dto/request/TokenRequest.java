package moheng.auth.dto.request;

public class TokenRequest {
    private String code;

    private TokenRequest() {}

    public TokenRequest(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}