package moheng.keyword.dto.request;

import java.util.List;
import java.util.Map;

public class TripRecommendByKeywordRequest {
    private List<String> keywords;
    private Map<Long, Long> preferredLocations;

    private TripRecommendByKeywordRequest() {
    }

    public TripRecommendByKeywordRequest(final List<String> keywords, final Map<Long, Long> preferredLocations) {
        this.keywords = keywords;
        this.preferredLocations = preferredLocations;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Map<Long, Long> getPreferredLocations() {
        return preferredLocations;
    }
}
