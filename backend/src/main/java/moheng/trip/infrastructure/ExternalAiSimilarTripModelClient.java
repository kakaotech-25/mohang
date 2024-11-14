package moheng.trip.infrastructure;

import moheng.global.cache.CacheConfig;
import moheng.keyword.exception.InvalidAIServerException;
import moheng.trip.domain.model.ExternalSimilarTripModelClient;
import moheng.trip.dto.response.FindSimilarTripWithContentIdResponses;
import moheng.trip.dto.request.SimilarTripRequests;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExternalAiSimilarTripModelClient implements ExternalSimilarTripModelClient {
    private static final String SIMILAR_TRIP_LIST_REQUEST_URL = "http://ai:8000/travel/similar/{contentId}?page={page}";
    private final RestTemplate restTemplate;

    public ExternalAiSimilarTripModelClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = CacheConfig.EXTERNAL_SIMILAR_TRIP_CACHE, key = "#contentId+#page")
    @Override
    public FindSimilarTripWithContentIdResponses findSimilarTrips(final long contentId, final long page) {
        return requestSimilarTrips(new SimilarTripRequests(contentId, page));
    }

    private FindSimilarTripWithContentIdResponses requestSimilarTrips(final SimilarTripRequests request) {
        Map<String, Long> uriPathVariable = new HashMap<>();
        uriPathVariable.put("contentId", request.getContentId());
        uriPathVariable.put("page", request.getPage());

        ResponseEntity<FindSimilarTripWithContentIdResponses> contentIdResponses = fetchSimilarTrips(uriPathVariable);
        return contentIdResponses.getBody();
    }

    private ResponseEntity<FindSimilarTripWithContentIdResponses> fetchSimilarTrips(final Map<String, Long> uriPathVariable) {
        try {
            return restTemplate.getForEntity(SIMILAR_TRIP_LIST_REQUEST_URL,
                    FindSimilarTripWithContentIdResponses.class, uriPathVariable);
        } catch (final ResourceAccessException | HttpClientErrorException e) {
            throw new InvalidAIServerException("AI 서버에 접근할 수 없는 상태입니다.");
        }
        catch (final RestClientException e) {
            throw new InvalidAIServerException("AI 서버에 예기치 못한 오류가 발생했습니다.");
        }
    }
}
