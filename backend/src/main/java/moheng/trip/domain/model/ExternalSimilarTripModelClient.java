package moheng.trip.domain.model;

import moheng.trip.dto.FindSimilarTripWithContentIdResponses;

public interface ExternalSimilarTripModelClient {
    FindSimilarTripWithContentIdResponses findSimilarTrips(final long contentId, final long page);
}
