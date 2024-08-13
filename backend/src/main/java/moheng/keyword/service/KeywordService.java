package moheng.keyword.service;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.MemberKeyword;
import moheng.keyword.domain.MemberKeywordRepository;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import org.springframework.stereotype.Service;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final KeywordFilterModelClient keyFilterModelClient;

    public KeywordService(final KeywordRepository keywordRepository, final KeywordFilterModelClient keyFilterModelClient) {
        this.keywordRepository = keywordRepository;
        this.keyFilterModelClient = keyFilterModelClient;
    }

    public void createKeyword(KeywordCreateRequest request) {
        keywordRepository.save(new Keyword(request.getKeyword()));
    }

    public void findRecommendTripsByKeywords(final TripRecommendByKeywordRequest request) {
        keyFilterModelClient.findRecommendTripsByKeywords(request);
    }
}
