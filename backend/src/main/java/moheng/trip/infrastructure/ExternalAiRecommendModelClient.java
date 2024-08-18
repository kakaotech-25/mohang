package moheng.trip.infrastructure;

import moheng.trip.dto.RecommendTripsByVisitedLogsRequest;
import moheng.trip.dto.RecommendTripsByVisitedLogsResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExternalAiRecommendModelClient {
    private static final String RECOMMEND_TRIP_LIST_REQUEST_URL = "http://localhost:8000/{page}";
    private final RestTemplate restTemplate;

    public ExternalAiRecommendModelClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogs(final RecommendTripsByVisitedLogsRequest request) {
        return requestRecommendTrips(request);
    }

    private RecommendTripsByVisitedLogsResponse requestRecommendTrips(final RecommendTripsByVisitedLogsRequest request) {
        final Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("page", String.valueOf(request.getPage()));

        final ResponseEntity<RecommendTripsByVisitedLogsResponse> responseEntity = restTemplate.exchange(
                RECOMMEND_TRIP_LIST_REQUEST_URL,
                HttpMethod.POST,
                new HttpEntity<>(request),
                RecommendTripsByVisitedLogsResponse.class,
                uriVariables
        );
        return responseEntity.getBody();
    }

}