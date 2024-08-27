package moheng.planner.dto;

import java.time.LocalDate;

public class UpdateTripScheduleRequest {
    private Long scheduleId;
    private String scheduleName;
    private LocalDate startDate;
    private LocalDate endDate;

    private UpdateTripScheduleRequest() {
    }

    public UpdateTripScheduleRequest(final Long scheduleId, final String scheduleName, final LocalDate startDate, final LocalDate endDate) {
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getScheduleId() {
        return scheduleId;
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
