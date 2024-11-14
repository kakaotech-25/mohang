package moheng.liveinformation.presentation;

import moheng.liveinformation.application.LiveInformationService;
import moheng.liveinformation.dto.response.FindAllLiveInformationResponse;
import moheng.liveinformation.dto.request.LiveInformationCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/live/info")
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
