package moheng.planner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UpdateTripScheduleRequest {

    @NotNull(message = "Null 일 수 없습니다.")
    private Long scheduleId;

    @NotBlank(message = "일정 이름은 공백일 수 없습니다.")
    private String scheduleName;

    @NotBlank(message = "시작날짜는 공백일 수 없습니다.")
    private LocalDate startDate;

    @NotBlank(message = "종료날짜는 공백일 수 없습니다.")
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
