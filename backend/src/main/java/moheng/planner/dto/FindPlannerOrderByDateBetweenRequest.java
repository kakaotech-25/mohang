package moheng.planner.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FindPlannerOrderByDateBetweenRequest {
    private LocalDate startDate;
    private LocalDate endDate;

    private FindPlannerOrderByDateBetweenRequest() {
    }

    public FindPlannerOrderByDateBetweenRequest(final LocalDate startDate, final LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate.atStartOfDay();
    }

    public LocalDateTime getEndDate() {
        return endDate.atTime(LocalTime.MAX);
    }
}