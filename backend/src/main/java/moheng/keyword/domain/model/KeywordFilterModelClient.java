package moheng.keyword.domain.model;

import moheng.keyword.dto.response.TripContentIdsByKeywordResponse;
import moheng.keyword.dto.request.TripRecommendByKeywordRequest;


public interface KeywordFilterModelClient {
    TripContentIdsByKeywordResponse findRecommendTripContentIdsByKeywords(final TripRecommendByKeywordRequest request);
}
