package moheng.keyword.domain;

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
}
