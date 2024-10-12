package moheng.recommendtrip.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.keyword.domain.TripKeyword;
import moheng.recommendtrip.application.RecommendTripService;
import moheng.recommendtrip.domain.filterinfo.PreferredLocationsFilterInfo;
import moheng.recommendtrip.domain.tripfilterstrategy.TripFilterStrategy;
import moheng.recommendtrip.domain.tripfilterstrategy.TripFilterStrategyProvider;
import moheng.recommendtrip.domain.tripfilterstrategy.TripsWithKeywordProvider;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/recommend")
@RestController
public class RecommendTripController {
    private static final String PREFERRED_LOCATIONS_STRATEGY = "PREFERRED_LOCATIONS";
    private final RecommendTripService recommendTripService;
    private final TripFilterStrategyProvider tripFilterStrategyProvider;
    private final TripsWithKeywordProvider tripsWithKeywordProvider;

    public RecommendTripController(final RecommendTripService recommendTripService,
                                   final TripFilterStrategyProvider tripFilterStrategyProvider,
                                   final TripsWithKeywordProvider tripsWithKeywordProvider) {
        this.recommendTripService = recommendTripService;
        this.tripFilterStrategyProvider = tripFilterStrategyProvider;
        this.tripsWithKeywordProvider = tripsWithKeywordProvider;
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
        final TripFilterStrategy tripFilterStrategy = tripFilterStrategyProvider.findTripsByFilterStrategy(PREFERRED_LOCATIONS_STRATEGY);
        return ResponseEntity.ok(tripsWithKeywordProvider.findWithKeywords(
                tripFilterStrategy.execute(new PreferredLocationsFilterInfo(accessor.getId())))
        );
    }
}
