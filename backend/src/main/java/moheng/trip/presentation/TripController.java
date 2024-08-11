package moheng.trip.presentation;

import moheng.trip.application.TripService;
import moheng.trip.dto.TripCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
