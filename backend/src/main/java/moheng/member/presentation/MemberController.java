package moheng.member.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.member.application.MemberService;
import moheng.member.dto.response.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getUserInfo(@Authentication final Accessor accessor) {
        System.out.println("accessor: " + accessor.getId());
        MemberResponse response = memberService.findById(accessor.getId());
        return ResponseEntity.ok(response);
    }
}
