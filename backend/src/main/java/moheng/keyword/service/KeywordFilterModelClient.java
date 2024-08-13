package moheng.keyword.service;

import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.exception.TripRecommendByKeywordRequest;

public interface KeywordFilterModelClient {
    TripContentIdsByKeywordResponse findRecommendTripContentIdsByKeywords(TripRecommendByKeywordRequest request);
}
