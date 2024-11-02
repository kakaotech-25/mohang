package moheng.trip.dto;

import java.util.List;

public class FindSimilarTripWithContentIdResponses {
    private List<Long> contentIds;

    private FindSimilarTripWithContentIdResponses() {
    }

    public FindSimilarTripWithContentIdResponses(final List<Long> contentIds) {
        this.contentIds = contentIds;
    }

    public List<Long> getContentIds() {
        return contentIds;
    }
}
