package moheng.config.stub;

import moheng.trip.domain.ExternalSimilarTripModelClient;
import moheng.trip.dto.FindSimilarTripWithContentIdResponses;

import java.util.List;

public class StubSimilarTripModelClient implements ExternalSimilarTripModelClient {
    private static final List<Long> contentIds = List.of(1L, 2L, 3L);

    @Override
    public FindSimilarTripWithContentIdResponses findSimilarTrips(long contentId) {
        return new FindSimilarTripWithContentIdResponses(contentIds);
    }
}
