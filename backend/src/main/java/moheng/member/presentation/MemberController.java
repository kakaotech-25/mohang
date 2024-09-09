package moheng.member.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.auth.presentation.initauthentication.InitAuthentication;
import moheng.member.application.MemberService;
import moheng.member.dto.request.*;
import moheng.member.dto.response.CheckDuplicateNicknameResponse;
import moheng.member.dto.response.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getUserInfo(@Authentication final Accessor accessor) {
        MemberResponse response = memberService.findById(accessor.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup/profile")
    public ResponseEntity<Void> signupProfile(@InitAuthentication final Accessor accessor,
                                              @RequestBody final SignUpProfileRequest signUpProfileRequest) {
        memberService.signUpByProfile(accessor.getId(), signUpProfileRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup/liveinfo")
    public ResponseEntity<Void> signupLiveInfo(@InitAuthentication final Accessor accessor,
                                                @RequestBody final SignUpLiveInfoRequest signUpLiveInfoRequest) {
        memberService.signUpByLiveInfo(accessor.getId(), signUpLiveInfoRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup/trip")
    public ResponseEntity<Void> signupInterestTrip(@InitAuthentication final Accessor accessor,
                                                    @RequestBody final SignUpInterestTripsRequest signUpInterestTripsRequest) {
        memberService.signUpByInterestTrips(accessor.getId(), signUpInterestTripsRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<CheckDuplicateNicknameResponse> checkDuplicateNickname(
            @Authentication final Accessor accessor,
            @RequestBody final CheckDuplicateNicknameRequest request) {
        memberService.checkIsAlreadyExistNickname(request.getNickname());
        return ResponseEntity.ok(new CheckDuplicateNicknameResponse());
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateMemberProfile(
            @Authentication final Accessor accessor,
            @RequestBody final UpdateProfileRequest request) {
        memberService.updateByProfile(accessor.getId(), request);
        return ResponseEntity.noContent().build();
    }
}
