package moheng.liveinformation.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.liveinformation.application.LiveInformationService;
import moheng.liveinformation.application.MemberLiveInformationService;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.dto.FindAllLiveInformationResponse;
import moheng.liveinformation.dto.FindMemberLiveInformationResponses;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
import moheng.liveinformation.dto.LiveInformationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/live/info")
@RestController
public class LiveInformationController {
    private final LiveInformationService liveInformationService;

    public LiveInformationController(final LiveInformationService liveInformationService) {
        this.liveInformationService = liveInformationService;
    }

    @GetMapping("/all")
    public ResponseEntity<FindAllLiveInformationResponse> findAllLiveInformation() {
        return ResponseEntity.ok(liveInformationService.findAllLiveInformation());
    }

    @PostMapping
    public ResponseEntity<Void> createLiveInformation(@RequestBody final LiveInformationCreateRequest request) {
        liveInformationService.createLiveInformation(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/trip/{tripId}/{liveInfoId}")
    public ResponseEntity<Void> createTripLiveInformation(@PathVariable final long tripId, @PathVariable final long liveInfoId) {
        liveInformationService.createTripLiveInformation(tripId, liveInfoId);
        return ResponseEntity.noContent().build();
    }
}
