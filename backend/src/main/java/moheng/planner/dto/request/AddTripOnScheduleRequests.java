package moheng.planner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class AddTripOnScheduleRequests {
    private List<Long> scheduleIds;

    private AddTripOnScheduleRequests() {
    }

    public AddTripOnScheduleRequests(final List<Long> scheduleIds) {
        this.scheduleIds = scheduleIds;
    }

    public List<Long> getScheduleIds() {
        return scheduleIds;
    }
}
