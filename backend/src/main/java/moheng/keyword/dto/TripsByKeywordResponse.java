package moheng.keyword.dto;

import java.util.List;

public class TripsByKeywordResponse {
    private List<Long> contentIds;

    public TripsByKeywordResponse(final List<Long> contentIds) {
        this.contentIds = contentIds;
    }

    public List<Long> getContentIds() {
        return contentIds;
    }
}
