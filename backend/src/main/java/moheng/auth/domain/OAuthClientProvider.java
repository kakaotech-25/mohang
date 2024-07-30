package moheng.auth.domain;

import moheng.auth.exception.NoExistOAuthClientException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OAuthClientProvider {
    private final List<OAuthClient> oAuthClients;

    public OAuthClientProvider(List<OAuthClient> oAuthClients) {
        this.oAuthClients = oAuthClients;
    }

    public OAuthClient getOauthClient(final String provider) {
        return oAuthClients.stream()
                .filter(oAuthClient -> oAuthClient.isSame(provider))
                .findFirst()
                .orElseThrow(NoExistOAuthClientException::new);
    }
}
