package moheng.auth.application;

import moheng.auth.domain.oauth.OAuthClient;
import moheng.auth.domain.oauth.OAuthMember;
import moheng.auth.domain.oauth.OAuthProvider;
import moheng.auth.domain.oauth.OAuthUriProvider;
import moheng.auth.domain.token.MemberToken;
import moheng.auth.domain.token.RenewalToken;
import moheng.auth.domain.token.TokenManager;
import moheng.auth.dto.LogoutRequest;
import moheng.auth.dto.RenewalAccessTokenRequest;
import moheng.auth.dto.RenewalAccessTokenResponse;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {
    private final OAuthProvider oAuthProvider;
    private final MemberService memberService;
    private final TokenManager tokenManager;

    public AuthService(final OAuthProvider oAuthProvider,
                       final MemberService memberService, final TokenManager tokenManager) {
        this.oAuthProvider = oAuthProvider;
        this.memberService = memberService;
        this.tokenManager = tokenManager;
    }

    @Transactional
    public MemberToken generateTokenWithCode(final String code, final String providerName) {
        final OAuthClient oAuthClient = oAuthProvider.getOauthClient(providerName);
        final OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        final Member foundMember = findOrCreateMember(oAuthMember, providerName);
        final MemberToken memberToken = tokenManager.createMemberToken(foundMember.getId());
        return memberToken;
    }

    private Member findOrCreateMember(final OAuthMember oAuthMember, final String providerName) {
        final String email = oAuthMember.getEmail();

        if (!memberService.existsByEmail(email)) {
            memberService.save(generateMember(oAuthMember, providerName));
        }
        final Member foundMember = memberService.findByEmail(email);
        return foundMember;
    }

    public String generateUri(final String providerName) {
        final OAuthUriProvider oAuthUriProvider = oAuthProvider.getOAuthUriProvider(providerName);
        return oAuthUriProvider.generateUri();
    }


    private Member generateMember(final OAuthMember oAuthMember, final String providerName) {
        return new Member(oAuthMember.getEmail(), oAuthProvider.getSocialType(providerName));
    }

    @Transactional
    public RenewalAccessTokenResponse generateRenewalAccessToken(final RenewalAccessTokenRequest renewalAccessTokenRequest) {
        final String refreshToken = renewalAccessTokenRequest.getRefreshToken();
        final RenewalToken renewalToken = tokenManager.generateRenewalAccessToken(refreshToken);
        return new RenewalAccessTokenResponse(renewalToken.getAccessToken());
    }

    @Transactional
    public void removeRefreshToken(final LogoutRequest logoutRequest) {
        tokenManager.removeRefreshToken(logoutRequest.getRefreshToken());
    }
}
