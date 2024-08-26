package moheng.planner.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.planner.application.TripScheduleService;
import moheng.planner.dto.CreateTripScheduleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/schedule")
@RestController
public class TripScheduleController {
    private final TripScheduleService tripScheduleService;

    public TripScheduleController(final TripScheduleService tripScheduleService) {
        this.tripScheduleService = tripScheduleService;
    }

    @PostMapping
    public ResponseEntity<Void> createTripSchedule(@Authentication final Accessor accessor,
                                                   @RequestBody final CreateTripScheduleRequest createTripScheduleRequest) {
        tripScheduleService.createTripSchedule(accessor.getId(), createTripScheduleRequest);
        return ResponseEntity.noContent().build();
    }
}
