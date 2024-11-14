package moheng.keyword.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class TripsByKeyWordsRequest {

    @NotEmpty(message = "키워드 리스트는 비어있을 수 없습니다.")
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
