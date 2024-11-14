package moheng.trip.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class TripKeywordCreateRequest {

    @NotNull(message = "여행지 ID는 공백일 수 없습니다.")
    private Long tripId;

    @NotNull(message = "키워드 ID는 공백일 수 없습니다.")
    private Long keywordId;

    private TripKeywordCreateRequest() {
    }

    public TripKeywordCreateRequest(final Long tripId, final Long keywordId) {
        this.tripId = tripId;
        this.keywordId = keywordId;
    }

    public Long getTripId() {
        return tripId;
    }

    public Long getKeywordId() {
        return keywordId;
    }
}
