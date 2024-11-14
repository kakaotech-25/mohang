package moheng.trip.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RecommendTripsByVisitedLogsRequest {
    @NotEmpty(message = "선호 여행지 정보는 비어있을 수 없습니다.")
    private List<LocationPreference> preferredLocation;

    @NotNull(message = "페이징을 위한 Page 값은 Null 일 수 없습니다.")
    private Long page;

    private RecommendTripsByVisitedLogsRequest() {
    }

    public RecommendTripsByVisitedLogsRequest(final List<LocationPreference> preferredLocation, final Long page) {
        this.preferredLocation = preferredLocation;
        this.page = page;
    }

    public List<LocationPreference> getPreferredLocation() {
        return preferredLocation;
    }

    public Long getPage() {
        return page;
    }

    public static class LocationPreference {
        private Long contentId;
        private Long clicked;

        public LocationPreference(final Long contentId, final Long clicked) {
            this.contentId = contentId;
            this.clicked = clicked;
        }

        public Long getContentId() {
            return contentId;
        }

        public void setContentId(Long contentId) {
            this.contentId = contentId;
        }

        public Long getClicked() {
            return clicked;
        }

        public void setClicked(Long clicked) {
            this.clicked = clicked;
        }
    }
}