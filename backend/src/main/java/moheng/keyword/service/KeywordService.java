package moheng.keyword.service;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final KeywordFilterModelClient keywordFilterModelClient;
    private final TripRepository tripRepository;

    public KeywordService(final KeywordRepository keywordRepository,
                          final KeywordFilterModelClient keywordFilterModelClient,
                          final TripRepository tripRepository) {
        this.keywordRepository = keywordRepository;
        this.keywordFilterModelClient = keywordFilterModelClient;
        this.tripRepository = tripRepository;
    }

    @Transactional
    public void createKeyword(KeywordCreateRequest request) {
        keywordRepository.save(new Keyword(request.getKeyword()));
    }

    @Transactional
    public FindTripsResponse findRecommendTripsByKeywords(final TripRecommendByKeywordRequest request) {
        final TripContentIdsByKeywordResponse response = keywordFilterModelClient.findRecommendTripContentIdsByKeywords(request);
        return new FindTripsResponse(findTripsByContentIds(response));
    }

    private List<Trip> findTripsByContentIds(final TripContentIdsByKeywordResponse contentIdsByKeywordsResponse) {
        return contentIdsByKeywordsResponse.getContentIds().stream()
                .map(contentId -> tripRepository.findByContentId(contentId)
                        .orElseThrow(NoExistTripException::new))
                .collect(Collectors.toList());
    }
}
