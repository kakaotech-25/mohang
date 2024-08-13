package moheng.keyword.infrastructure;

import moheng.auth.domain.oauth.OAuthAccessToken;
import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.keyword.service.KeywordFilterModelClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
public class AiKeywordModelClient implements KeywordFilterModelClient {
    private static final String RECOMMEND_TRIP_LIST_BY_KEYWORD_REQUEST_URL = "http://localhost:8000";
    private final RestTemplate restTemplate;

    public AiKeywordModelClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TripContentIdsByKeywordResponse findRecommendTripContentIdsByKeywords(TripRecommendByKeywordRequest request) {
        return requestRecommendTrips(request);
    }

    private TripContentIdsByKeywordResponse requestRecommendTrips(TripRecommendByKeywordRequest request) {
        final ResponseEntity<TripContentIdsByKeywordResponse> contentIds = restTemplate.postForEntity(RECOMMEND_TRIP_LIST_BY_KEYWORD_REQUEST_URL, request, TripContentIdsByKeywordResponse.class);
        return Optional.ofNullable(contentIds.getBody())
                .orElseThrow(IllegalArgumentException::new);
    }
}
