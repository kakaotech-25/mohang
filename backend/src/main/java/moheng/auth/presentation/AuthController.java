package moheng.auth.presentation;

import moheng.auth.application.AuthService;
import moheng.auth.domain.MemberToken;
import moheng.auth.dto.*;
import moheng.auth.presentation.authentication.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/{provider}/link")
    public ResponseEntity<OAuthUriResponse> generateUri(@PathVariable final String provider) {
        return ResponseEntity.ok(new OAuthUriResponse(authService.generateUri()));
    }

    @PostMapping("/{oauthProvider}/login")
    public ResponseEntity<MemberToken> login(@PathVariable("oauthProvider") final String oAuthProvider,
                                             @RequestBody final TokenRequest tokenRequest) {
        MemberToken tokenResponse = authService.generateTokenWithCode(tokenRequest.getCode(), oAuthProvider);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/extend/login")
    public ResponseEntity<RenewalAccessTokenResponse> extendLogin(@RequestBody final RenewalAccessTokenRequest renewalAccessTokenRequest) {
        final RenewalAccessTokenResponse renewalAccessTokenResponse =
                authService.generateRenewalAccessToken(renewalAccessTokenRequest);
        return ResponseEntity.ok(renewalAccessTokenResponse);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@Authentication final Accessor accessor,
                                       @RequestBody final LogoutRequest logoutRequest) {
        authService.removeRefreshToken(logoutRequest);
        return ResponseEntity.noContent().build();
    }
}
