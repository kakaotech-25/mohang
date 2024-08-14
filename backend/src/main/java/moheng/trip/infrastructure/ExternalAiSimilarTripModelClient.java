package moheng.trip.infrastructure;

import moheng.trip.domain.ExternalSimilarTripModelClient;
import moheng.trip.dto.SimilarTripResponses;
import org.springframework.stereotype.Component;

@Component
public class ExternalAiSimilarTripModelClient implements ExternalSimilarTripModelClient {
    @Override
    public SimilarTripResponses findSimilarTrips(long contentId) {
        return null;
    }
}
