package moheng.keyword.application;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.statistics.TripsByStatisticsFinder;
import moheng.keyword.domain.random.RandomKeywordGeneratable;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.keyword.dto.FindAllKeywordResponses;
import moheng.keyword.dto.FindTripsWithRandomKeywordResponse;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.exception.NoExistKeywordException;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripKeywordCreateRequest;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.domain.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class KeywordService {
    private final TripsByStatisticsFinder tripsByStatisticsFinder;
    private static final int RECOMMEND_BY_KEYWORD_TRIPS_COUNT = 30;
    private final RandomKeywordGeneratable randomKeywordGeneratable;
    private final KeywordRepository keywordRepository;
    private final TripRepository tripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public KeywordService(final TripsByStatisticsFinder tripsByStatisticsFinder,
                          final RandomKeywordGeneratable randomKeywordGeneratable,
                          final KeywordRepository keywordRepository,
                          final TripRepository tripRepository,
                          final TripKeywordRepository tripKeywordRepository) {
        this.tripsByStatisticsFinder = tripsByStatisticsFinder;
        this.randomKeywordGeneratable = randomKeywordGeneratable;
        this.keywordRepository = keywordRepository;
        this.tripRepository = tripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
    }

    public FindAllKeywordResponses findAllKeywords() {
        return new FindAllKeywordResponses(keywordRepository.findAll());
    }

    @Transactional
    public void createKeyword(final KeywordCreateRequest request) {
        keywordRepository.save(new Keyword(request.getKeyword()));
    }

    public List<String> findNamesByIds(final TripsByKeyWordsRequest request) {
        return keywordRepository.findNamesByIds(request.getKeywordIds());
    }

    public FindTripsResponse findRecommendTripsByKeywords(final TripsByKeyWordsRequest request) {
        if (request.getKeywordIds().isEmpty()) {
            return findRecommendTripsByEmptyKeyword();
        }
        validateKeywords(request.getKeywordIds());
        final List<TripKeyword> tripKeywords = filterTop30Trips(tripKeywordRepository.findTripKeywordsByKeywordIds(request.getKeywordIds()));
        final List<Trip> topTrips = tripsByStatisticsFinder.findTripsWithVisitedCount(tripKeywords);
        return new FindTripsResponse(extractAllTripKeywordsByTopTrips(topTrips));
    }

    private FindTripsResponse findRecommendTripsByEmptyKeyword() {
        final Keyword randomKeyword = randomKeywordGeneratable.generate();
        final List<TripKeyword> tripKeywords = tripKeywordRepository.findTop30ByKeywordId(randomKeyword.getId());
        final List<Trip> topTrips = tripsByStatisticsFinder.findTripsWithVisitedCount(tripKeywords);
        return new FindTripsResponse(extractAllTripKeywordsByTopTrips(topTrips));
    }

    private List<TripKeyword> filterTop30Trips(final List<TripKeyword> trips) {
        return trips.stream()
                .limit(RECOMMEND_BY_KEYWORD_TRIPS_COUNT)
                .collect(Collectors.toList());
    }

    private void validateKeywords(final List<Long> keywordIds) {
        final List<Long> existingKeywords = findAllByIds(keywordIds);
        if (existingKeywords.size() != keywordIds.size()) {
            throw new NoExistKeywordException("일부 키워드가 존재하지 않습니다.");
        }
    }

    private List<Long> findAllByIds(final List<Long> keywordIds) {
        return keywordRepository.findAllById(keywordIds)
                .stream().map(Keyword::getId)
                .collect(Collectors.toList());
    }

    public FindTripsWithRandomKeywordResponse findRecommendTripsByRandomKeyword() {
        final Keyword randomKeyword = randomKeywordGeneratable.generate();
        final List<TripKeyword> tripKeywords = tripKeywordRepository.findTop30ByKeywordId(randomKeyword.getId());
        final List<Trip> topTrips = tripsByStatisticsFinder.findTripsWithVisitedCount(tripKeywords);
        return new FindTripsWithRandomKeywordResponse(extractAllTripKeywordsByTopTrips(topTrips), randomKeyword);
    }

    private List<TripKeyword> extractAllTripKeywordsByTopTrips(final List<Trip> topTrips) {
        final Map<Trip, List<TripKeyword>> tripWithKeywords = findTripsWithTripKeywords(topTrips);
        return topTrips.stream()
                .flatMap(trip -> tripWithKeywords.getOrDefault(trip, Collections.emptyList()).stream())
                .collect(Collectors.toList());
    }

    private Map<Trip, List<TripKeyword>> findTripsWithTripKeywords(final List<Trip> topTrips) {
        return tripKeywordRepository.findByTripIn(topTrips)
                .stream()
                .collect(Collectors.groupingBy(TripKeyword::getTrip));
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
