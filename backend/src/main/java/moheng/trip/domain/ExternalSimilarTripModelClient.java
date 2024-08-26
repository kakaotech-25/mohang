package moheng.trip.domain;

import moheng.trip.dto.FindSimilarTripWithContentIdResponses;
import moheng.trip.dto.SimilarTripResponses;

public interface ExternalSimilarTripModelClient {
    FindSimilarTripWithContentIdResponses findSimilarTrips(final long contentId, final long page);
}
