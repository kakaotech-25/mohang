package moheng.recommendtrip.domain.tripfilterstrategy;

import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.recommendtrip.exception.LackOfRecommendTripException;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TripsWithKeywordProvider {
    private static final int RECOMMEND_TRIPS_COUNT = 10;
    private final TripKeywordRepository tripKeywordRepository;

    public TripsWithKeywordProvider(final TripKeywordRepository tripKeywordRepository) {
        this.tripKeywordRepository = tripKeywordRepository;
    }

    public FindTripsResponse findWithKeywords(final List<Trip> trips) {
        if(trips.size() != RECOMMEND_TRIPS_COUNT) {
            throw new LackOfRecommendTripException("여행지 추천 결과는 정확히 10개가 추천되어야합니다.");
        }
        return new FindTripsResponse(tripKeywordRepository.findByTrips(trips));
    }
}
