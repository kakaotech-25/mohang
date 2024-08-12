package moheng.auth.presentation;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;

import jakarta.persistence.Access;
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

    @GetMapping("/{oAuthProvider}/link")
    public ResponseEntity<OAuthUriResponse> generateUri(@PathVariable final String oAuthProvider) {
        return ResponseEntity.ok(new OAuthUriResponse(authService.generateUri(oAuthProvider)));
    }

    @PostMapping("/{oAuthProvider}/login")
    public ResponseEntity<AccessTokenResponse> login(@PathVariable final String oAuthProvider,
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
        final AccessTokenResponse accessTokenResponse = new AccessTokenResponse(memberToken.getAccessToken());
        return ResponseEntity.status(CREATED).body(accessTokenResponse);
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
                                       @CookieValue("refresh-token") final String refreshToken) {
        authService.removeRefreshToken(new LogoutRequest(refreshToken));
        return ResponseEntity.noContent().build();
    }
}
