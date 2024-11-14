package moheng.config.stub;

import moheng.keyword.dto.response.TripContentIdsByKeywordResponse;
import moheng.keyword.dto.request.TripRecommendByKeywordRequest;
import moheng.keyword.domain.model.KeywordFilterModelClient;

import java.util.List;

public class StubKeywordFilterModelClient implements KeywordFilterModelClient {

    @Override
    public TripContentIdsByKeywordResponse findRecommendTripContentIdsByKeywords(TripRecommendByKeywordRequest request) {
        return new TripContentIdsByKeywordResponse(List.of(1L, 2L, 3L));
    }
}
