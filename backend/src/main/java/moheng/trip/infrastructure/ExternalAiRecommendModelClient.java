package moheng.trip.infrastructure;

import moheng.keyword.exception.InvalidAIServerException;
import moheng.recommendtrip.dto.PreferredLocationRequest;
import moheng.trip.domain.model.ExternalRecommendModelClient;
import moheng.trip.dto.RecommendTripsByVisitedLogsRequest;
import moheng.trip.dto.RecommendTripsByVisitedLogsResponse;
import moheng.trip.dto.RecommendTripsRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExternalAiRecommendModelClient implements ExternalRecommendModelClient {
    private static final String RECOMMEND_TRIP_LIST_REQUEST_URL = "http://ai:8000/travel/custom/model?page={page}";
    private final RestTemplate restTemplate;

    public ExternalAiRecommendModelClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public RecommendTripsByVisitedLogsResponse recommendTripsByVisitedLogs(final RecommendTripsByVisitedLogsRequest request) {
        return requestRecommendTrips(request);
    }

    private RecommendTripsByVisitedLogsResponse requestRecommendTrips(final RecommendTripsByVisitedLogsRequest request) {
        final Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("page", String.valueOf(request.getPage()));

        final ResponseEntity<RecommendTripsByVisitedLogsResponse> responseEntity = restTemplate.exchange(
                RECOMMEND_TRIP_LIST_REQUEST_URL,
                HttpMethod.POST,
                new HttpEntity<>(new PreferredLocationRequest(request.getPreferredLocation())),
                RecommendTripsByVisitedLogsResponse.class,
                uriVariables
        );

        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        throw new InvalidAIServerException("AI 서버에 예기치 못한 오류가 발생했습니다.");
    }

}