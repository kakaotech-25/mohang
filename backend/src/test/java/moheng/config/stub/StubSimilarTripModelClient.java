package moheng.config.stub;

import moheng.trip.domain.ExternalSimilarTripModelClient;
import moheng.trip.dto.FindSimilarTripWithContentIdResponses;

import java.util.List;

public class StubSimilarTripModelClient implements ExternalSimilarTripModelClient {
    private static final List<Long> contentIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);

    @Override
    public FindSimilarTripWithContentIdResponses findSimilarTrips(long contentId) {
        return new FindSimilarTripWithContentIdResponses(contentIds);
    }
}
