package moheng.keyword.dto;

import java.util.List;

public class TripsByKeyWordsRequest {
    private List<Long> keywordIds;

    private TripsByKeyWordsRequest() {
    }

    public TripsByKeyWordsRequest(final List<Long> keywordIds) {
        this.keywordIds = keywordIds;
    }

    public List<Long> getKeywordIds() {
        return keywordIds;
    }
}
