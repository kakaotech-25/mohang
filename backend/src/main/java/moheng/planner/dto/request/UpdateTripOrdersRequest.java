package moheng.planner.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UpdateTripOrdersRequest {

    @NotEmpty(message = "여행지 ID 리스트는 공백일 수 없습니다.")
    private List<Long> tripIds;

    private UpdateTripOrdersRequest() {
    }

    public UpdateTripOrdersRequest(final List<Long> tripIds) {
        this.tripIds = tripIds;
    }

    public List<Long> getTripIds() {
        return tripIds;
    }
}
