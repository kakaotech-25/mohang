package moheng.keyword.service;

import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.dto.TripRecommendByKeywordRequest;


public interface KeywordFilterModelClient {
    TripContentIdsByKeywordResponse findRecommendTripContentIdsByKeywords(TripRecommendByKeywordRequest request);
}
