package moheng.auth.presentation;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;

import jakarta.servlet.http.HttpServletResponse;
import moheng.auth.application.AuthService;
import moheng.auth.domain.token.MemberToken;
import moheng.auth.dto.*;
import moheng.auth.presentation.authentication.Authentication;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/{oauthProvider}/link")
    public ResponseEntity<OAuthUriResponse> generateUri(@PathVariable final String oauthProvider) {
        return ResponseEntity.ok(new OAuthUriResponse(authService.generateUri(oauthProvider)));
    }

    @PostMapping("/{oauthProvider}/login")
    public ResponseEntity<AccessTokenResponse> login(@PathVariable("oauthProvider") final String oAuthProvider,
                                             @RequestBody final TokenRequest tokenRequest,
                                             final HttpServletResponse httpServletResponse) {
        final MemberToken memberToken = authService.generateTokenWithCode(tokenRequest.getCode(), oAuthProvider);
        final ResponseCookie responseCookie = ResponseCookie.from("refresh-token", memberToken.getRefreshToken())
                .maxAge(604800)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
        httpServletResponse.addHeader(SET_COOKIE, responseCookie.toString());
        return ResponseEntity.status(CREATED)
                .body(new AccessTokenResponse(memberToken.getAccessToken()));
    }

    @PostMapping("/extend/login")
    public ResponseEntity<RenewalAccessTokenResponse> extendLogin(
            @CookieValue("refresh-token") final String refreshToken) {
        final RenewalAccessTokenResponse renewalAccessTokenResponse =
                authService.generateRenewalAccessToken(new RenewalAccessTokenRequest(refreshToken));
        return ResponseEntity.status(CREATED).body(renewalAccessTokenResponse);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@Authentication final Accessor accessor,
                                       @RequestBody final LogoutRequest logoutRequest) {
        authService.removeRefreshToken(logoutRequest);
        return ResponseEntity.noContent().build();
    }
}
