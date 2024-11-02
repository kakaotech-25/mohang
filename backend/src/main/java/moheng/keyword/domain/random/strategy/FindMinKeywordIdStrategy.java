package moheng.keyword.domain.random.strategy;

import moheng.keyword.domain.repository.KeywordRepository;
import org.springframework.stereotype.Component;

@Component
public class FindMinKeywordIdStrategy implements FindKeywordStrategy {
    private final String STRATEGY_NAME = "RANDOM_MIN";
    private final KeywordRepository keywordRepository;

    public FindMinKeywordIdStrategy(final KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    @Override
    public Long findKeywordId() {
        return keywordRepository.findMinKeywordId();
    }

    @Override
    public boolean isMatch(final String strategyName) {
        return strategyName.equals(STRATEGY_NAME);
    }
}
