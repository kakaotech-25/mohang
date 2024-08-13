package moheng.keyword.infrastructure;

import moheng.keyword.dto.TripsByKeywordResponse;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.keyword.service.KeywordFilterModelClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiKeywordModelClient implements KeywordFilterModelClient {
    private static final String KEYWORD_LIST_REQUEST_URL = "http://localhost:8000";
    private final RestTemplate restTemplate;

    public AiKeywordModelClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public TripsByKeywordResponse findRecommendTripsByKeywords(TripRecommendByKeywordRequest request) {
        return null;
    }
}
