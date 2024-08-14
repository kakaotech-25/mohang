package moheng.trip.domain;

import moheng.trip.dto.SimilarTripResponses;

public interface ExternalSimilarTripModelClient {
    SimilarTripResponses findSimilarTrips(long contentId);
}
