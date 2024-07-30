package moheng.auth.application;

import moheng.auth.domain.*;
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
                       final MemberService memberService, TokenManager tokenManager) {
        this.oAuthProvider = oAuthProvider;
        this.memberService = memberService;
        this.tokenManager = tokenManager;
    }

    @Transactional
    public MemberToken generateTokenWithCode(final String code, final String providerName) {
        final OAuthClient oAuthClient = oAuthProvider.getOauthClient(providerName);
        final OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        final Member foundMember = findOrCreateMember(oAuthMember);
        final MemberToken memberToken = tokenManager.createMemberToken(foundMember.getId());
        return memberToken;
    }

    private Member findOrCreateMember(OAuthMember oAuthMember) {
        final String email = oAuthMember.getEmail();

        if (!memberService.existsByEmail(email)) {
            memberService.save(generateMember(oAuthMember));
        }
        final Member foundMember = memberService.findByEmail(email);
        return foundMember;
    }

    public String generateUri(String providerName) {
        final OAuthUriProvider oAuthUriProvider = oAuthProvider.getOAuthUriProvider(providerName);
        return oAuthUriProvider.generateUri();
    }


    private Member generateMember(final OAuthMember oAuthMember) {
        return new Member(oAuthMember.getEmail(), SocialType.KAKAO);
    }

    @Transactional
    public RenewalAccessTokenResponse generateRenewalAccessToken(final RenewalAccessTokenRequest renewalAccessTokenRequest) {
        String refreshToken = renewalAccessTokenRequest.getRefreshToken();
        String renewalAccessToken = tokenManager.generateRenewalAccessToken(refreshToken);
        return new RenewalAccessTokenResponse(renewalAccessToken);
    }

    @Transactional
    public void removeRefreshToken(final LogoutRequest logoutRequest) {
        tokenManager.removeRefreshToken(logoutRequest.getRefreshToken());
    }
}
