package moheng.planner.dto;

import moheng.planner.domain.TripSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TripScheduleResponse {
    private Long scheduleId;
    private String scheduleName;
    private LocalDate startTime;
    private LocalDate endTime;

    private TripScheduleResponse() {
    }

    public TripScheduleResponse(final TripSchedule tripSchedule) {
        this.scheduleId = tripSchedule.getId();
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

}
