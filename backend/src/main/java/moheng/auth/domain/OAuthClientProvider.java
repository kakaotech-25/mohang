package moheng.auth.domain;

import moheng.auth.exception.NoExistOAuthClientException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OAuthClientProvider {
    private final List<OAuthClient> oAuthClients;
    private final List<OAuthUriProvider> oAuthUriProviders;

    public OAuthClientProvider(List<OAuthClient> oAuthClients, List<OAuthUriProvider> oAuthUriProviders) {
        this.oAuthClients = oAuthClients;
        this.oAuthUriProviders = oAuthUriProviders;
    }

    public OAuthClient getOauthClient(final String provider) {
        return oAuthClients.stream()
                .filter(oAuthClient -> oAuthClient.isSame(provider))
                .findFirst()
                .orElseThrow(NoExistOAuthClientException::new);
    }

    public OAuthUriProvider getOAuthUriProvider(final String provider) {
        return oAuthUriProviders.stream()
                .filter(oAuthUriProvider -> oAuthUriProvider.isSame(provider))
                .findFirst()
                .orElseThrow(NoExistOAuthClientException::new);
    }
}
