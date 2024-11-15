package moheng.planner.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.planner.application.PlannerService;
import moheng.planner.dto.request.FindPlannerOrderByDateBetweenRequest;
import moheng.planner.dto.request.FindPublicSchedulesForCurrentMonthResponses;
import moheng.planner.dto.request.FindPublicSchedulesForRangeRequest;
import moheng.planner.dto.request.UpdateTripScheduleRequest;
import moheng.planner.dto.response.FindPLannerOrderByNameResponse;
import moheng.planner.dto.response.FindPlannerOrderByDateBetweenResponse;
import moheng.planner.dto.response.FindPlannerOrderByDateResponse;
import moheng.planner.dto.response.FindPlannerOrderByRecentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/planner")
@RestController
public class PlannerController {
    private final PlannerService plannerService;

    public PlannerController(final PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @GetMapping("/recent")
    public ResponseEntity<FindPlannerOrderByRecentResponse> findOrderByRecent(@Authentication final Accessor accessor) {
        return ResponseEntity.ok(plannerService.findPlannerOrderByRecent(accessor.getId()));
    }

    @GetMapping("/name")
    public ResponseEntity<FindPLannerOrderByNameResponse> findOrderByName(@Authentication final Accessor accessor) {
        return ResponseEntity.ok(plannerService.findPlannerOrderByName(accessor.getId()));
    }

    @GetMapping("/date")
    public ResponseEntity<FindPlannerOrderByDateResponse> findOrderByDate(@Authentication final Accessor accessor) {
        return ResponseEntity.ok(plannerService.findPlannerOrderByDateAsc(accessor.getId()));
    }

    @PutMapping("/schedule")
    public ResponseEntity<Void> updatePlannerTripSchedule(@Authentication final Accessor accessor,
                                                          @RequestBody final UpdateTripScheduleRequest updateTripScheduleRequest) {
        plannerService.updateTripSchedule(accessor.getId(), updateTripScheduleRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/schedule/{scheduleId}")
    public ResponseEntity<Void> deletePlannerTripSchedule(@Authentication final Accessor accessor,
                                                          @PathVariable final Long scheduleId) {
        plannerService.removeTripSchedule(scheduleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/range")
    public ResponseEntity<FindPlannerOrderByDateBetweenResponse> findPlannerOrderByDateAndRange(@Authentication final Accessor accessor,
                                                                                                @RequestBody final FindPlannerOrderByDateBetweenRequest findPlannerOrderByDateBetweenRequest) {
        return ResponseEntity.ok(plannerService.findPlannerOrderByDateAndRange(accessor.getId(), findPlannerOrderByDateBetweenRequest));
    }

    @GetMapping("/month")
    public ResponseEntity<FindPublicSchedulesForCurrentMonthResponses> findPublicSchedulesForCurrentMonth(@Authentication final Accessor accessor) {
        return ResponseEntity.ok(plannerService.findPublicSchedulesForCurrentMonth());
    }

    @GetMapping("/search/date")
    public ResponseEntity<FindPlannerOrderByDateBetweenResponse> findPublicSchedulesForCreatedAtRange(@Authentication final Accessor accessor,
                                                                                                            @RequestBody final FindPublicSchedulesForRangeRequest findPlannerOrderByDateBetweenRequest,
                                                                                                            @PageableDefault(size = 100) final Pageable pageable) {
        return ResponseEntity.ok(plannerService.findPublicSchedulesForCreatedAtRange(findPlannerOrderByDateBetweenRequest, pageable));
    }
}
