package moheng.auth.presentation;

import moheng.auth.application.AuthService;
import moheng.auth.domain.MemberToken;
import moheng.auth.dto.*;
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

    @PostMapping("/{provider}/login")
    public ResponseEntity<MemberToken> login(@PathVariable final String provider,
                                             @RequestBody final TokenRequest tokenRequest) {
        MemberToken tokenResponse = authService.generateTokenWithCode(tokenRequest.getCode());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/extend/login")
    public ResponseEntity<RenewalAccessTokenResponse> extendLogin(final RenewalAccessTokenRequest renewalAccessTokenRequest) {
        final RenewalAccessTokenResponse renewalAccessTokenResponse =
                authService.generateRenewalAccessToken(renewalAccessTokenRequest);
        return ResponseEntity.ok(renewalAccessTokenResponse);
    }
}
