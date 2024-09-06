package moheng.keyword.domain;

import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.dto.TripRecommendByKeywordRequest;


public interface KeywordFilterModelClient {
    TripContentIdsByKeywordResponse findRecommendTripContentIdsByKeywords(final TripRecommendByKeywordRequest request);
}
