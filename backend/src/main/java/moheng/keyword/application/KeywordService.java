package moheng.keyword.application;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.exception.NoExistKeywordException;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripKeywordCreateRequest;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.domain.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final TripRepository tripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public KeywordService(final KeywordRepository keywordRepository,
                          final TripRepository tripRepository,
                          final TripKeywordRepository tripKeywordRepository) {
        this.keywordRepository = keywordRepository;
        this.tripRepository = tripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
    }

    @Transactional
    public void createKeyword(final KeywordCreateRequest request) {
        keywordRepository.save(new Keyword(request.getKeyword()));
    }

    public List<String> findNamesByIds(final TripsByKeyWordsRequest request) {
        return keywordRepository.findNamesByIds(request.getKeywordIds());
    }

    public FindTripsResponse findRecommendTripsByKeywords(final TripsByKeyWordsRequest request) {
        final List<TripKeyword> trips = tripKeywordRepository.findTripKeywordsByKeywordIds(request.getKeywordIds());
        return new FindTripsResponse(trips);
    }

    public FindTripsResponse findRecommendTripsByRandomKeyword() {
        final Keyword randomKeyword = findRandomKeyword();
        final List<TripKeyword> tripKeywords = tripKeywordRepository.findTripKeywordsByKeywordId(randomKeyword.getId());
        final Map<Trip, Long> tripsWithVisitedCount = findTripsWithVisitedCount(tripKeywords);
        final List<Trip> topTrips = findTopTripsByVisitedCount(tripsWithVisitedCount);
        return new FindTripsResponse(extractTripKeywordsByTopTrips(topTrips, tripKeywords));
    }

    private Keyword findRandomKeyword() {
        final Long minId = keywordRepository.findMinKeywordId();
        final Long maxId = keywordRepository.findMaxKeywordId();
        validateKeywordRange(minId, maxId);
        final Long randomId = generateRandomId(minId, maxId);
        return keywordRepository.findKeywordById(randomId)
                .orElseThrow(() -> new NoExistKeywordException("랜덤 키워드를 찾을 수 없습니다."));
    }

    private void validateKeywordRange(final Long minId, final Long maxId) {
        if (minId == null || maxId == null) {
            throw new NoExistKeywordException("랜덤 키워드를 찾을 수 없습니다.");
        }
    }

    private Long generateRandomId(final Long minId, final Long maxId) {
        final SecureRandom secureRandom = new SecureRandom();
        return minId + secureRandom.nextLong(maxId - minId + 1);
    }

    private Map<Trip, Long> findTripsWithVisitedCount(final List<TripKeyword> tripKeywords) {
        final Map<Trip, Long> tripClickCounts = new HashMap<>();
        for (TripKeyword tripKeyword : tripKeywords) {
            Trip trip = tripKeyword.getTrip();
            tripClickCounts.put(tripKeyword.getTrip(), trip.getVisitedCount());
        }
        return tripClickCounts;
    }

    private List<Trip> findTopTripsByVisitedCount(final Map<Trip, Long> tripsWithVisitedCount) {
        return tripsWithVisitedCount.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(30)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<TripKeyword> extractTripKeywordsByTopTrips(final List<Trip> topTrips, final List<TripKeyword> tripKeywords) {
        return topTrips.stream()
                .flatMap(topTrip -> tripKeywords.stream()
                        .filter(tripKeyword -> tripKeyword.getTrip().equals(topTrip)))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createTripKeyword(final TripKeywordCreateRequest request) {
        final Trip trip = tripRepository.findById(request.getTripId())
                        .orElseThrow(NoExistTripException::new);
        final Keyword keyword = keywordRepository.findById(request.getKeywordId())
                        .orElseThrow(NoExistKeywordException::new);
        tripKeywordRepository.save(new TripKeyword(trip, keyword));
    }
}
