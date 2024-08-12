package moheng.trip.presentation;

import moheng.trip.application.TripService;
import moheng.trip.dto.FindTripsOrderByVisitedCountDescResponse;
import moheng.trip.dto.TripCreateRequest;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/trip")
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
    public ResponseEntity<FindTripsOrderByVisitedCountDescResponse> findTopTripsOrderByVisitedCount() {
        return ResponseEntity.ok(tripService.findTop30OrderByVisitedCountDesc());
    }
}
