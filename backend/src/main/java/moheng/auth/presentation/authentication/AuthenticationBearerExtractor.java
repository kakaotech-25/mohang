package moheng.auth.presentation.authentication;

import jakarta.servlet.http.HttpServletRequest;
import moheng.auth.exception.EmptyBearerHeaderException;
import moheng.auth.exception.InvalidTokenFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthenticationBearerExtractor {
    private static final String BEARER_TYPE = "Bearer ";

    private AuthenticationBearerExtractor() {
    }

    public String extract(final HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        validateEmptyHeader(authorizationHeader);
        validateAuthorizationFormat(authorizationHeader);

        return authorizationHeader.substring(BEARER_TYPE.length()).trim();
    }

    private void validateAuthorizationFormat(final String authorizationHeader) {
        if(!authorizationHeader.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            throw new InvalidTokenFormatException("유효하지 않은 토큰 형식입니다.");
        }
    }

    private void validateEmptyHeader(String authorizationHeader) {
        if(Objects.isNull(authorizationHeader)) {
            throw new EmptyBearerHeaderException("Authorization Bearer 해더 값이 비어있습니다.");
        }
    }
}
