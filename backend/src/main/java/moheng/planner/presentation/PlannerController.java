package moheng.planner.presentation;

import moheng.planner.application.PlannerService;
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
    public ResponseEntity<Void> findOrderByRecent() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name")
    public ResponseEntity<Void> findOrderByName() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/date")
    public ResponseEntity<Void> findOrderByDate() {
        return ResponseEntity.noContent().build();
    }
}
