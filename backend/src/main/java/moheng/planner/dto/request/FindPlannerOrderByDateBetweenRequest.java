package moheng.planner.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FindPlannerOrderByDateBetweenRequest {

    @NotBlank(message = "시작날짜는 공백일 수 없습니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotBlank(message = "종료날짜는 공백일 수 없습니다.")
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
