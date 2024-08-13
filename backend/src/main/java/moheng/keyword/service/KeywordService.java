package moheng.keyword.service;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.MemberKeyword;
import moheng.keyword.domain.MemberKeywordRepository;
import moheng.keyword.dto.KeywordCreateRequest;
import org.springframework.stereotype.Service;

@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public KeywordService(final KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    public void createKeyword(KeywordCreateRequest request) {
        keywordRepository.save(new Keyword(request.getKeyword()));
    }
}
