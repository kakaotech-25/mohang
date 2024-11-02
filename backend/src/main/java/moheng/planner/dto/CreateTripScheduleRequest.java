package moheng.planner.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateTripScheduleRequest {
    private final String scheduleName;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public CreateTripScheduleRequest(final String scheduleName, final LocalDate startDate, final LocalDate endDate) {
        this.scheduleName = scheduleName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
