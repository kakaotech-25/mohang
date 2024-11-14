package moheng.trip.domain.model;

import moheng.trip.dto.response.FindSimilarTripWithContentIdResponses;

public interface ExternalSimilarTripModelClient {
    FindSimilarTripWithContentIdResponses findSimilarTrips(final long contentId, final long page);
}
