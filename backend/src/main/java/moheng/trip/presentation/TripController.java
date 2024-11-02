package moheng.trip.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.trip.application.TripService;
import moheng.trip.dto.FindTripWithSimilarTripsResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/trip")
@RestController
public class TripController {
    private final TripService tripService;

    public TripController(final TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<Void> createTrip(@RequestBody final TripCreateRequest tripCreateRequest) {
        tripService.createTrip(tripCreateRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find/interested")
    public ResponseEntity<FindTripsResponse> findTopTripsOrderByVisitedCount() {
        return ResponseEntity.ok(tripService.findTop30OrderByVisitedCountDesc());
    }

    @GetMapping("/find/{tripId}")
    public ResponseEntity<FindTripWithSimilarTripsResponse> findTripWithSimilarTrips(@PathVariable("tripId") final long tripId,
                                                                                     @Authentication final Accessor accessor) {
        return ResponseEntity.ok(tripService.findWithSimilarOtherTrips(tripId , accessor.getId()));
    }

    @PostMapping("/member/{tripId}")
    public ResponseEntity<Void> createMemberTrip(@Authentication final Accessor accessor,
                                                 @PathVariable("tripId") final long tripId) {
        tripService.createMemberTrip(accessor.getId(), tripId);
        return ResponseEntity.noContent().build();
    }
}
