package moheng.keyword.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.random.strategy.FindMaxKeywordStrategy;
import moheng.keyword.domain.random.strategy.FindMinKeywordIdStrategy;
import moheng.keyword.domain.repository.KeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FindKeywordStrategyTest extends ServiceTestConfig {
    @Autowired
    private FindMaxKeywordStrategy findMaxKeywordStrategy;

    @Autowired
    private FindMinKeywordIdStrategy findMinKeywordIdStrategy;

    @Autowired
    private KeywordRepository keywordRepository;

    @DisplayName("ID 값이 가장 큰 키워드의 ID를 리턴한다.")
    @Test
    void ID_값이_가장_큰_키워드의_ID를_리턴한다() {
        // given
        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));
        keywordRepository.save(new Keyword("키워드3"));

        // when
        long actual = findMaxKeywordStrategy.findKeywordId();

        // then
        assertEquals(actual, 3L);
    }

    @DisplayName("ID 값이 가장 작은 키워드의 ID를 리턴한다.")
    @Test
    void ID_값이_가장_작은_키워드의_ID를_리턴한다() {
        // given
        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));
        keywordRepository.save(new Keyword("키워드3"));

        // when
        long actual = findMinKeywordIdStrategy.findKeywordId();

        // then
        assertEquals(actual, 1L);
    }

    @DisplayName("최소 ID 키워드 찾기 전략의 매핑 조건은 RANDOM_MIN 이다.")
    @Test
    void 최소_ID_키워드_찾기_전략의_매핑_조건은_RANDOM_MIN_이다() {
        // given
        String STRATEGY_NAME = "RANDOM_MIN";

        // when, then
        assertTrue(findMinKeywordIdStrategy.isMatch(STRATEGY_NAME));
    }
}
