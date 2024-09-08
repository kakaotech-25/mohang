package moheng.keyword.application;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripsByStatisticsFinder;
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
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripKeywordCreateRequest;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.domain.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class KeywordService {
    private final TripsByStatisticsFinder tripStatisticsFinder;
    private static final int RECOMMEND_BY_KEYWORD_TRIPS_COUNT = 30;
    private static final int TOP_TRIPS_COUNT = 30;
    private final RandomKeywordGeneratable randomKeywordGeneratable;
    private final KeywordRepository keywordRepository;
    private final TripRepository tripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public KeywordService(final TripsByStatisticsFinder tripStatisticsFinder,
                          final RandomKeywordGeneratable randomKeywordGeneratable,
                          final KeywordRepository keywordRepository,
                          final TripRepository tripRepository,
                          final TripKeywordRepository tripKeywordRepository) {
        this.tripStatisticsFinder = tripStatisticsFinder;
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
        validateKeywords(request.getKeywordIds());
        final List<TripKeyword> trips = filterTop30Trips(tripKeywordRepository.findTripKeywordsByKeywordIds(request.getKeywordIds()));
        return new FindTripsResponse(trips);
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
        final Keyword randomKeyword = findRandomKeyword();
        final List<TripKeyword> tripKeywords = tripKeywordRepository.findTop30ByKeywordId(randomKeyword.getId());
        final List<Trip> topTrips = tripStatisticsFinder.findTripsWithVisitedCount(tripKeywords);
        return new FindTripsWithRandomKeywordResponse(extractAllTripKeywordsByTopTrips(topTrips), randomKeyword);
    }

    private Keyword findRandomKeyword() {
        return randomKeywordGeneratable.generate();
    }

    private List<TripKeyword> extractAllTripKeywordsByTopTrips(final List<Trip> topTrips) {
        return topTrips.stream()
                .flatMap(topTrip -> tripKeywordRepository.findByTrip(topTrip).stream())
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
