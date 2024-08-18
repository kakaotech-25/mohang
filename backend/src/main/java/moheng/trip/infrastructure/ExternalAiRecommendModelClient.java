package moheng.trip.infrastructure;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalAiRecommendModelClient {
    private static final String SIMILAR_TRIP_LIST_REQUEST_URL = "http://localhost:8000/{contentId}";
    private final RestTemplate restTemplate;

    public ExternalAiRecommendModelClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void recommendTripsByVisitedLogs() {

    }
}
