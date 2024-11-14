package moheng.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public class TokenRequest {
    @NotNull(message = "리프레시 토큰은 공백일 수 없습니다.")
    private String code;

    private TokenRequest() {}

    public TokenRequest(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}