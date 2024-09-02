package moheng.planner.dto;

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
