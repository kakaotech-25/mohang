package moheng.liveinformation.presentation;

import moheng.liveinformation.application.LiveInformationService;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.dto.LiveInformationCreateRequest;
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

    @PostMapping
    public ResponseEntity<Void> createLiveInformation(final @RequestBody LiveInformationCreateRequest request) {
        liveInformationService.createLiveInformation(request);
        return ResponseEntity.noContent().build();
    }
}
