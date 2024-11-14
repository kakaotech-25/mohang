package moheng.recommendtrip.dto;

import jakarta.validation.constraints.NotNull;

public class RecommendTripCreateRequest {

    @NotNull(message = "Null 일 수 없습니다.")
    private Long tripId;

    private RecommendTripCreateRequest() {
    }

    public RecommendTripCreateRequest(final Long tripId) {
        this.tripId = tripId;
    }

    public Long getTripId() {
        return tripId;
    }
}
