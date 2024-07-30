package moheng.auth.domain.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.auth.exception.InvalidOAuthServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class KakaoOAuthClient implements OAuthClient {
    private final String PROVIDER_NAME = "kakao";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String redirectUri;
    private final String clientId;
    private final String clientSecret;
    private final String tokenUri;
    private final String userUri;
    private final String grantType = "authorization_code";

    public KakaoOAuthClient(final RestTemplate restTemplate,
                            final ObjectMapper objectMapper,
                            @Value("${oauth.kakao.redirect_uri}") final String redirectUri,
                            @Value("${oauth.kakao.client_id}") final String clientId,
                            @Value("${oauth.kakao.client_secret}") final String clientSecret,
                            @Value("${oauth.kakao.token_uri}") final String tokenUri,
                            @Value("${oauth.kakao.user_uri}") final String userUri) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.tokenUri = tokenUri;
        this.userUri = userUri;
    }

    @Override
    public boolean isSame(String providerName) {
        return PROVIDER_NAME.equals(providerName);
    }

    @Override
    public OAuthMember getOAuthMember(final String code) {
        final String accessToken = requestKakaoAccessToken(code);
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        final HttpEntity<MultiValueMap<String, String>> userInfoRequestEntity = new HttpEntity<>(httpHeaders);

        final Map<String, Boolean> queryParam = new HashMap<>();
        queryParam.put("secure_resource", Boolean.TRUE);

        final ResponseEntity<OAuthMember> kakaoOAuthMember = restTemplate.exchange(
                userUri,
                HttpMethod.GET,
                userInfoRequestEntity,
                OAuthMember.class,
                queryParam
        );

        if(kakaoOAuthMember.getStatusCode().is2xxSuccessful()) {
            return kakaoOAuthMember.getBody();
        }
        throw new InvalidOAuthServiceException("카카오 OAuth 소셜 로그인 서버에 예기치 못한 오류가 발생했습니다.");
    }

    private String requestKakaoAccessToken(String code) {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(clientId, clientSecret);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", grantType);

        final HttpEntity<MultiValueMap<String, String>> accessTokenRequestEntity = new HttpEntity<>(params, httpHeaders);
        final ResponseEntity<OAuthAccessToken> accessToken = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                accessTokenRequestEntity,
                OAuthAccessToken.class
        );

        return Optional.ofNullable(accessToken.getBody())
                .orElseThrow(IllegalArgumentException::new)
                .getAccessToken();
    }
}
