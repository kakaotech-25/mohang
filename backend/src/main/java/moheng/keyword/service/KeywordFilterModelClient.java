package moheng.keyword.service;

import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import org.springframework.stereotype.Component;


public interface KeywordFilterModelClient {
    TripContentIdsByKeywordResponse findRecommendTripContentIdsByKeywords(TripRecommendByKeywordRequest request);
}
