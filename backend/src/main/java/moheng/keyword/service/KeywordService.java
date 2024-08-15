package moheng.keyword.service;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.RecommendTripResponse;
import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.dto.TripRecommendByKeywordRequest;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.domain.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final RecommendTripRepository recommendTripRepository;
    private final TripRepository tripRepository;
    private final TripKeywordRepository tripKeywordRepository;

    public KeywordService(final KeywordRepository keywordRepository,
                          final RecommendTripRepository recommendTripRepository,
                          final TripRepository tripRepository,
                          final TripKeywordRepository tripKeywordRepository) {
        this.keywordRepository = keywordRepository;
        this.tripRepository = tripRepository;
        this.recommendTripRepository = recommendTripRepository;
        this.tripKeywordRepository = tripKeywordRepository;
    }

    @Transactional
    public void createKeyword(KeywordCreateRequest request) {
        keywordRepository.save(new Keyword(request.getKeyword()));
    }

    public List<String> findNamesByIds(final TripsByKeyWordsRequest request) {
        return keywordRepository.findNamesByIds(request.getKeywordIds());
    }

    public FindTripsResponse findRecommendTripsByKeywords(final TripsByKeyWordsRequest request) {
        final List<TripKeyword> tripWithKeywords = tripKeywordRepository.findTripsByKeywordIds(request.getKeywordIds());
        return new FindTripsResponse(tripWithKeywords);
    }
}
