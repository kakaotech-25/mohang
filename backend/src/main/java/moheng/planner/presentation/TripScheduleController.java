package moheng.planner.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.planner.application.TripScheduleService;
import moheng.planner.dto.request.AddTripOnScheduleRequests;
import moheng.planner.dto.request.CreateTripScheduleRequest;
import moheng.planner.dto.response.FindTripsOnSchedule;
import moheng.planner.dto.request.UpdateTripOrdersRequest;
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

    @PostMapping("/trip/{tripId}")
    public ResponseEntity<Void> addTripOnSchedule(@Authentication final Accessor accessor,
                                                  @PathVariable("tripId") final Long tripId,
                                                  @RequestBody final AddTripOnScheduleRequests addTripOnScheduleRequests) {
        tripScheduleService.addCurrentTripOnPlannerSchedule(tripId, addTripOnScheduleRequests);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trips/{scheduleId}")
    public ResponseEntity<FindTripsOnSchedule> findTripsOnSchedule(@Authentication final Accessor accessor,
                                                                   @PathVariable("scheduleId") final Long scheduleId) {
        return ResponseEntity.ok(tripScheduleService.findTripsOnSchedule(scheduleId));
    }

    @PostMapping("/trips/orders/{scheduleId}")
    public ResponseEntity<Void> updateTripOrdersOnSchedule(@Authentication final Accessor accessor,
                                                           @PathVariable("scheduleId") final Long scheduleId,
                                                           @RequestBody final UpdateTripOrdersRequest updateTripOrdersRequest) {
        tripScheduleService.updateTripOrdersOnSchedule(scheduleId, updateTripOrdersRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{scheduleId}/{tripId}")
    public ResponseEntity<Void> deleteTripOnSchedule(@Authentication final Accessor accessor,
                                                     @PathVariable("scheduleId") final Long scheduleId,
                                                     @PathVariable("tripId") final Long tripId) {
        tripScheduleService.deleteTripOnSchedule(scheduleId, tripId);
        return ResponseEntity.noContent().build();
    }
}
