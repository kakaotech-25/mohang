package moheng.auth.domain.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoUriProvider implements OAuthUriProvider {
    private static final String KAKAO = "KAKAO";
    private final String authorizationUri;
    private final String clientId;
    private final String redirectUri;
    private final String response_type = "code";

    public KakaoUriProvider(@Value("${oauth.kakao.authorize_uri}") final String authorizationUri,
                            @Value("${oauth.kakao.client_id}") final String clientId,
                            @Value("${oauth.kakao.redirect_uri}") final String redirectUri){
        this.authorizationUri = authorizationUri;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    @Override
    public String generateUri() {
        return authorizationUri + "?"
                + "client_id=" + clientId
//                + "&redirect_uri=" + redirectUri
                + "&response_type=" + response_type;
    }

    @Override
    public boolean isSame(String provider) {
        return KAKAO.equals(provider);
    }

}
