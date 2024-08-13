package moheng.keyword.service;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.RecommendTripResponse;
import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class KeywordService {
    private final KeywordFilterModelClient keywordFilterModelClient;
    private final KeywordRepository keywordRepository;
    private final RecommendTripRepository recommendTripRepository;
    private final TripRepository tripRepository;

    public KeywordService(final KeywordRepository keywordRepository,
                          final KeywordFilterModelClient keywordFilterModelClient,
                          final RecommendTripRepository recommendTripRepository,
                          final TripRepository tripRepository) {
        this.keywordRepository = keywordRepository;
        this.keywordFilterModelClient = keywordFilterModelClient;
        this.tripRepository = tripRepository;
        this.recommendTripRepository = recommendTripRepository;
    }

    @Transactional
    public void createKeyword(KeywordCreateRequest request) {
        keywordRepository.save(new Keyword(request.getKeyword()));
    }

    public List<String> findNamesByIds(final TripsByKeyWordsRequest request) {
        return keywordRepository.findNamesByIds(request.getKeywordIds());
    }

    @Transactional
    public FindTripsResponse findRecommendTripsByKeywords(final long memberId, final TripsByKeyWordsRequest request) {
        final List<String> keywords = findNamesByIds(request);
        final TripRecommendByKeywordRequest tripRecommendByKeywordRequest = new TripRecommendByKeywordRequest(keywords, findRecommendTripsInfo(memberId));
        final TripContentIdsByKeywordResponse response = keywordFilterModelClient.findRecommendTripContentIdsByKeywords(tripRecommendByKeywordRequest);
        return new FindTripsResponse(findTripsByContentIds(response));
    }

    public Map<Long, Long> findRecommendTripsInfo(Long memberId) {
        final List<RecommendTripResponse> results = recommendTripRepository.findVisitedCountAndTripContentIdByMemberId(memberId);
        Map<Long, Long> contentIdToVisitedCountMap = results.stream()
                .collect(Collectors.toMap(RecommendTripResponse::getContentId, RecommendTripResponse::getVisitedCount));
        return contentIdToVisitedCountMap;
    }

    private List<Trip> findTripsByContentIds(final TripContentIdsByKeywordResponse contentIdsByKeywordsResponse) {
        return contentIdsByKeywordsResponse.getContentIds().stream()
                .map(contentId -> tripRepository.findByContentId(contentId)
                        .orElseThrow(NoExistTripException::new))
                .collect(Collectors.toList());
    }
}
