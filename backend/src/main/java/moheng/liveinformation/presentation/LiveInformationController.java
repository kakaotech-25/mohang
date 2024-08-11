package moheng.liveinformation.presentation;

import moheng.liveinformation.application.LiveInformationService;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.dto.FindAllLiveInformationResponse;
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

    @GetMapping
    public ResponseEntity<FindAllLiveInformationResponse> findAllLiveInformation() {
        return ResponseEntity.ok(liveInformationService.findAllLiveInformation());
    }

    @PostMapping
    public ResponseEntity<Void> createLiveInformation(final @RequestBody LiveInformationCreateRequest request) {
        liveInformationService.createLiveInformation(request);
        return ResponseEntity.noContent().build();
    }
}
