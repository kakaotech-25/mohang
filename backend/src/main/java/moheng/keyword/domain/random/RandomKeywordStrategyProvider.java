package moheng.keyword.domain.random;

import moheng.keyword.domain.random.strategy.FindKeywordStrategy;
import moheng.keyword.exception.NoExistKeywordException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RandomKeywordStrategyProvider {
    private final List<FindKeywordStrategy> findKeywordStrategies;

    public RandomKeywordStrategyProvider(final List<FindKeywordStrategy> findKeywordStrategies) {
        this.findKeywordStrategies = findKeywordStrategies;
    }

    public FindKeywordStrategy findKeywordStrategy(final String strategyName) {
        return findKeywordStrategies.stream()
                .filter(findKeywordStrategy -> findKeywordStrategy.isMatch(strategyName))
                .findFirst()
                .orElseThrow(NoExistKeywordException::new);
    }
}
