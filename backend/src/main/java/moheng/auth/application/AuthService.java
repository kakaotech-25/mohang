package moheng.auth.application;

import moheng.auth.domain.JwtTokenProvider;
import moheng.auth.domain.OAuthClient;
import moheng.auth.domain.OAuthMember;
import moheng.auth.domain.OAuthUriProvider;
import moheng.auth.dto.TokenResponse;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import moheng.member.domain.SocialType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final OAuthUriProvider oAuthUriProvider;
    private final OAuthClient oAuthClient;
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final OAuthUriProvider oAuthUriProvider, final OAuthClient oAuthClient,
                       final MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.oAuthUriProvider = oAuthUriProvider;
        this.oAuthClient = oAuthClient;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public TokenResponse generateTokenWithCode(final String code) {
        final OAuthMember oAuthMember = oAuthClient.getOAuthMember(code);
        final String email = oAuthMember.getEmail();

        if(!memberService.existsByEmail(email)) {
            memberService.save(generateMember(oAuthMember));
        }
        final Member foundMember = memberService.findByEmail(email);
        final String accessToken = jwtTokenProvider.createToken(String.valueOf(foundMember.getId()));

        return new TokenResponse(accessToken);
    }

    @Transactional(readOnly = true)
    public String generateUri() {
        return oAuthUriProvider.generateUri();
    }

    private Member generateMember(final OAuthMember oAuthMember) {
        return new Member(oAuthMember.getEmail(), SocialType.KAKAO);
    }
}
