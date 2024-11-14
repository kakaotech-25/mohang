package moheng.trip.dto.request;

import java.util.List;

public class RecommendTripsByVisitedLogsRequest {
    private List<LocationPreference> preferredLocation;
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