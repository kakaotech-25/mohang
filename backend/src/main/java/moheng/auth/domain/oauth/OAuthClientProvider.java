package moheng.auth.domain.oauth;

import moheng.auth.exception.NoExistOAuthClientException;
import moheng.global.annotation.Generated;
import moheng.member.domain.SocialType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OAuthClientProvider implements OAuthProvider {
    private final List<OAuthClient> oAuthClients;
    private final List<OAuthUriProvider> oAuthUriProviders;

    public OAuthClientProvider(List<OAuthClient> oAuthClients, List<OAuthUriProvider> oAuthUriProviders) {
        this.oAuthClients = oAuthClients;
        this.oAuthUriProviders = oAuthUriProviders;
    }

    @Override
    public OAuthClient getOauthClient(final String provider) {
        return oAuthClients.stream()
                .filter(oAuthClient -> oAuthClient.isSame(provider))
                .findFirst()
                .orElseThrow(NoExistOAuthClientException::new);
    }

    @Override
    public OAuthUriProvider getOAuthUriProvider(final String provider) {
        return oAuthUriProviders.stream()
                .filter(oAuthUriProvider -> oAuthUriProvider.isSame(provider))
                .findFirst()
                .orElseThrow(NoExistOAuthClientException::new);
    }

    @Override
    public SocialType getSocialType(final String provider) {
        return SocialType.findByName(provider);
    }
}
