package moheng.trip.infrastructure;

import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.exception.InvalidAIServerException;
import moheng.trip.domain.ExternalSimilarTripModelClient;
import moheng.trip.dto.FindSimilarTripWithContentIdResponses;
import moheng.trip.dto.SimilarTripRequests;
import moheng.trip.dto.SimilarTripResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExternalAiSimilarTripModelClient implements ExternalSimilarTripModelClient {
    private static final String SIMILAR_TRIP_LIST_REQUEST_URL = "http://localhost:8000/{contentId}";
    private final RestTemplate restTemplate;

    public ExternalAiSimilarTripModelClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public FindSimilarTripWithContentIdResponses findSimilarTrips(long contentId) {
        return requestSimilarTrips(new SimilarTripRequests(contentId));
    }

    private FindSimilarTripWithContentIdResponses requestSimilarTrips(final SimilarTripRequests request) {
        Map<String, Long> uriPathVariable = new HashMap<>();
        uriPathVariable.put("contentId", request.getContentId());

        ResponseEntity<FindSimilarTripWithContentIdResponses> contentIdResponses
                = restTemplate.getForEntity(SIMILAR_TRIP_LIST_REQUEST_URL, FindSimilarTripWithContentIdResponses.class, uriPathVariable);

        if(contentIdResponses.getStatusCode().is2xxSuccessful()) {
            return contentIdResponses.getBody();
        }
        throw new InvalidAIServerException("AI 서버에 예기치 못한 오류가 발생했습니다.");
    }
}
