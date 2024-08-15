package moheng.config.stub;

import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.dto.TripRecommendByKeywordRequest;
import moheng.keyword.application.KeywordFilterModelClient;

import java.util.List;

public class StubKeywordFilterModelClient implements KeywordFilterModelClient {

    @Override
    public TripContentIdsByKeywordResponse findRecommendTripContentIdsByKeywords(TripRecommendByKeywordRequest request) {
        return new TripContentIdsByKeywordResponse(List.of(1L, 2L, 3L));
    }
}
