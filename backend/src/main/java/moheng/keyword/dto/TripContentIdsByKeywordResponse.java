package moheng.keyword.dto;

import java.util.List;

public class TripContentIdsByKeywordResponse {
    private List<Long> contentIds;

    public TripContentIdsByKeywordResponse(final List<Long> contentIds) {
        this.contentIds = contentIds;
    }

    public List<Long> getContentIds() {
        return contentIds;
    }
}
