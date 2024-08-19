package moheng.recommendtrip.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.global.annotation.Generated;
import moheng.member.dto.request.SignUpInterestTripsRequest;
import moheng.recommendtrip.application.RecommendTripService;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.dto.FindTripsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/recommend")
@RestController
public class RecommendTripController {
    private final RecommendTripService recommendTripService;

    public RecommendTripController(final RecommendTripService recommendTripService) {
        this.recommendTripService = recommendTripService;
    }

    @PostMapping
    public ResponseEntity<Void> createRecommendTrip(
            @Authentication final Accessor accessor,
            @RequestBody final RecommendTripCreateRequest request) {
        recommendTripService.createRecommendTrip(accessor.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<FindTripsResponse> findRecommendTripsByModel(@Authentication final Accessor accessor) {
        return ResponseEntity.ok(recommendTripService.findRecommendTripsByModel(accessor.getId()));
    }
}
