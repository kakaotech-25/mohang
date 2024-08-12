package moheng.liveinformation.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.liveinformation.application.MemberLiveInformationService;
import moheng.liveinformation.dto.FindMemberLiveInformationResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/live/info/member")
@RestController
public class MemberLiveInformationController {
    private final MemberLiveInformationService memberLiveInformationService;

    public MemberLiveInformationController(MemberLiveInformationService memberLiveInformationService) {
        this.memberLiveInformationService = memberLiveInformationService;
    }

    @GetMapping
    public ResponseEntity<FindMemberLiveInformationResponses> findAllLiveInformationByUser(@Authentication Accessor accessor) {
        return ResponseEntity.ok(memberLiveInformationService.findMemberSelectedLiveInformation(accessor.getId()));
    }
}
