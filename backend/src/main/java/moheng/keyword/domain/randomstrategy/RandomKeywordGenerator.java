package moheng.keyword.domain.randomstrategy;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.exception.NoExistKeywordException;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RandomKeywordGenerator implements RandomKeywordGeneratable {
    private final KeywordRepository keywordRepository;

    public RandomKeywordGenerator(final KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @Override
    public Keyword generate() {
        final Long randomId = generateRandomId(findMinKeywordId(), findMaxKeywordId());
        return keywordRepository.findKeywordById(randomId)
                .orElseThrow(() -> new NoExistKeywordException("랜덤 키워드를 찾을 수 없습니다."));
    }

    private Long generateRandomId(final Long minId, final Long maxId) {
        final SecureRandom secureRandom = new SecureRandom();
        return minId + secureRandom.nextLong(maxId - minId + 1);
    }

    private Long findMinKeywordId() {
        return keywordRepository.findMinKeywordId();
    }

    private Long findMaxKeywordId() {
        return keywordRepository.findMaxKeywordId();
    }
}
