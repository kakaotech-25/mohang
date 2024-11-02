package moheng.keyword.domain.random.strategy;

public interface FindKeywordStrategy {
    Long findKeywordId();
    boolean isMatch(final String strategyName);
}
