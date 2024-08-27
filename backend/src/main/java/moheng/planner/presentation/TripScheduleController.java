package moheng.planner.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.planner.application.TripScheduleService;
import moheng.planner.dto.CreateTripScheduleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/trip/{tripId}/{scheduleId}")
    public ResponseEntity<Void> addTripOnSchedule(@Authentication final Accessor accessor,
                                                  @PathVariable("tripId") final Long tripId,
                                                  @PathVariable("scheduleId") final Long scheduleId) {
        tripScheduleService.addCurrentTripOnPlannerSchedule(tripId, scheduleId);
        return ResponseEntity.noContent().build();
    }
}
