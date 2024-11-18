package moheng.planner.dto.request;


public class FindSchedulesByNameRequest {
    private String name;

    private FindSchedulesByNameRequest() {
    }

    public FindSchedulesByNameRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
