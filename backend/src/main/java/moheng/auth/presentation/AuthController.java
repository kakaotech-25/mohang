package moheng.auth.presentation;

import moheng.auth.application.AuthService;
import moheng.auth.domain.MemberToken;
import moheng.auth.dto.OAuthUriResponse;
import moheng.auth.dto.TokenRequest;
import moheng.auth.dto.TokenResponse;
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

    @PostMapping("/{oauthProvider}/token")
    public ResponseEntity<MemberToken> login(@PathVariable final String oauthProvider,
                                                     @RequestBody final TokenRequest tokenRequest) {
        MemberToken tokenResponse = authService.generateTokenWithCode(tokenRequest.getCode());
        return ResponseEntity.ok(tokenResponse);
    }
}
