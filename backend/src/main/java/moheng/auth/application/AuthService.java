package moheng.auth.application;

import moheng.auth.domain.*;
import moheng.auth.dto.LogoutRequest;
import moheng.auth.dto.RenewalAccessTokenRequest;
import moheng.auth.dto.RenewalAccessTokenResponse;
import moheng.auth.dto.TokenResponse;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthService {
    private final OAuthClientProvider oAuthClientProvider;
    private final OAuthUriProvider oAuthUriProvider;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final OAuthClientProvider oAuthClientProvider,
                       final OAuthUriProvider oAuthUriProvider,
                       final MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.oAuthClientProvider = oAuthClientProvider;
        this.oAuthUriProvider = oAuthUriProvider;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public MemberToken generateTokenWithCode(final String code, final String oAuthProvider) {
        OAuthClient oAuthClient = oAuthClientProvider.getOauthClient(oAuthProvider);
        final OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        final String email = oAuthMember.getEmail();

        if(!memberService.existsByEmail(email)) {
            memberService.save(generateMember(oAuthMember));
        }
        final Member foundMember = memberService.findByEmail(email);
        final MemberToken memberToken = jwtTokenProvider.createMemberToken(foundMember.getId());
        return memberToken;
    }

    public String generateUri() {
        return oAuthUriProvider.generateUri();
    }


    private Member generateMember(final OAuthMember oAuthMember) {
        return new Member(oAuthMember.getEmail(), SocialType.KAKAO);
    }

    @Transactional
    public RenewalAccessTokenResponse generateRenewalAccessToken(final RenewalAccessTokenRequest renewalAccessTokenRequest) {
        String refreshToken = renewalAccessTokenRequest.getRefreshToken();
        String renewalAccessToken = jwtTokenProvider.generateRenewalAccessToken(refreshToken);
        return new RenewalAccessTokenResponse(renewalAccessToken);
    }

    @Transactional
    public void removeRefreshToken(final LogoutRequest logoutRequest) {
        jwtTokenProvider.removeRefreshToken(logoutRequest.getRefreshToken());
    }
}
