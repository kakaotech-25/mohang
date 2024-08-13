package moheng.keyword.service;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.MemberKeyword;
import moheng.keyword.domain.MemberKeywordRepository;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripContentIdsByKeywordResponse;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripResponse;
import moheng.trip.dto.FindTripsOrderByVisitedCountDescResponse;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final KeywordFilterModelClient keyFilterModelClient;
    private final TripRepository tripRepository;

    public KeywordService(final KeywordRepository keywordRepository,
                          final KeywordFilterModelClient keyFilterModelClient,
                          final TripRepository tripRepository) {
        this.keywordRepository = keywordRepository;
        this.keyFilterModelClient = keyFilterModelClient;
        this.tripRepository = tripRepository;
    }

    public void createKeyword(KeywordCreateRequest request) {
        keywordRepository.save(new Keyword(request.getKeyword()));
    }

    public FindTripsOrderByVisitedCountDescResponse findRecommendTripsByKeywords(final TripRecommendByKeywordRequest request) {
        final TripContentIdsByKeywordResponse response = keyFilterModelClient.findRecommendTripContentIdsByKeywords(request);
        return new FindTripsOrderByVisitedCountDescResponse(findTripsByContentIds(response));
    }

    private List<Trip> findTripsByContentIds(final TripContentIdsByKeywordResponse contentIdsByKeywordsResponse) {
        return contentIdsByKeywordsResponse.getContentIds().stream()
                .map(contentId -> tripRepository.findByContentId(contentId)
                        .orElseThrow(NoExistTripException::new))
                .collect(Collectors.toList());
    }
}
