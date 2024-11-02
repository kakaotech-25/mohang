package moheng.keyword.domain.random.strategy;

import moheng.keyword.domain.repository.KeywordRepository;
import org.springframework.stereotype.Component;

@Component
public class FindMaxKeywordStrategy implements FindKeywordStrategy {
    private final String STRATEGY_NAME = "RANDOM_MAX";
    private final KeywordRepository keywordRepository;

    public FindMaxKeywordStrategy(final KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @Override
    public Long findKeywordId() {
        return keywordRepository.findMaxKeywordId();
    }

    @Override
    public boolean isMatch(final String strategyName) {
        return strategyName.equals(STRATEGY_NAME);
    }
}
