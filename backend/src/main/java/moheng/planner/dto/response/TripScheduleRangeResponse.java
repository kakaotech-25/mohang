package moheng.planner.dto.response;

import moheng.planner.domain.TripSchedule;

import java.time.LocalDate;

public class TripScheduleRangeResponse {
    private Long scheduleId;
    private String scheduleName;
    private String description;
    private LocalDate startTime;
    private LocalDate endTime;

    private TripScheduleRangeResponse() {
    }

    public TripScheduleRangeResponse(final TripSchedule tripSchedule) {
        this.scheduleId = tripSchedule.getId();
        this.description = tripSchedule.getDescription();
        this.scheduleName = tripSchedule.getName();
        this.startTime = tripSchedule.getStartDate();
        this.endTime = tripSchedule.getEndDate();
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }
}
