package moheng.auth.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OAuthClientProvider {
    private final List<OAuthClient> oAuthClients;

    public OAuthClientProvider(final List<OAuthClient> oAuthClients) {
        this.oAuthClients = oAuthClients;
    }

    public OAuthClient getOauthClient(final String provider) {
       return null;
    }
}
