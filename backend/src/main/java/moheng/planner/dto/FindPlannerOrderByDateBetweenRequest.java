package moheng.planner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FindPlannerOrderByDateBetweenRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
