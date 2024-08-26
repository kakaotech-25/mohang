package moheng.planner.presentation;

import moheng.auth.dto.Accessor;
import moheng.auth.presentation.authentication.Authentication;
import moheng.planner.application.PlannerService;
import moheng.planner.dto.FindPLannerOrderByNameResponse;
import moheng.planner.dto.FindPlannerOrderByDateResponse;
import moheng.planner.dto.FindPlannerOrderByRecentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
