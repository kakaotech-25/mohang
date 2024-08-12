package moheng.liveinformation.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.liveinformation.application.MemberLiveInformationService;
import moheng.liveinformation.dto.FindMemberLiveInformationResponses;
import moheng.liveinformation.dto.UpdateMemberLiveInformationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/live/info/member")
@RestController
public class MemberLiveInformationController {
    private final MemberLiveInformationService memberLiveInformationService;

    public MemberLiveInformationController(MemberLiveInformationService memberLiveInformationService) {
        this.memberLiveInformationService = memberLiveInformationService;
    }

    @GetMapping
    public ResponseEntity<FindMemberLiveInformationResponses> findAllLiveInformationByMember(@Authentication final Accessor accessor) {
        return ResponseEntity.ok(memberLiveInformationService.findMemberSelectedLiveInformation(accessor.getId()));
    }

    @PutMapping
    public ResponseEntity<FindMemberLiveInformationResponses> updateMemberLiveInformation(@Authentication final Accessor accessor,
                                                                                          final UpdateMemberLiveInformationRequest request) {
        memberLiveInformationService.updateMemberLiveInformation(accessor.getId(), request);
        return ResponseEntity.noContent().build();
    }
}
