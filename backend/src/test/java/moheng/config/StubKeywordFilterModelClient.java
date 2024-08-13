package moheng.config;

import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.keyword.service.KeywordFilterModelClient;
import org.springframework.stereotype.Component;

import java.util.List;

public class StubKeywordFilterModelClient implements KeywordFilterModelClient {

    @Override
    public TripContentIdsByKeywordResponse findRecommendTripContentIdsByKeywords(TripRecommendByKeywordRequest request) {
        return new TripContentIdsByKeywordResponse(List.of(1L, 2L, 3L));
    }
}