package moheng.planner.dto;

import moheng.planner.domain.TripSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TripScheduleResponse {
    private String scheduleName;
    private LocalDate startTime;
    private LocalDate endTime;

    private TripScheduleResponse() {
    }

    public TripScheduleResponse(final TripSchedule tripSchedule) {
        this.scheduleName = tripSchedule.getName();
        this.startTime = tripSchedule.getStartDate();
        this.endTime = tripSchedule.getEndDate();
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
