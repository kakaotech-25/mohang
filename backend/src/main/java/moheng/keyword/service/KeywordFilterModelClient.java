package moheng.keyword.service;

import moheng.keyword.dto.TripsByKeywordResponse;
import moheng.keyword.exception.TripRecommendByKeywordRequest;

public interface KeywordFilterModelClient {
    TripsByKeywordResponse findRecommendTripsByKeywords(TripRecommendByKeywordRequest request);
}
