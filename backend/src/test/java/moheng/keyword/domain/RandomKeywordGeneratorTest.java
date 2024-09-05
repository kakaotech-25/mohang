package moheng.keyword.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.random.RandomKeywordGenerator;
import moheng.keyword.domain.repository.KeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RandomKeywordGeneratorTest extends ServiceTestConfig {
    @Autowired
    private RandomKeywordGenerator randomKeywordGenerator;

    @Autowired
    private KeywordRepository keywordRepository;

    @DisplayName("랜덤 키워드를 생성한다.")
    @Test
    void 랜덤_키워드를_생성한다() {
        // given
        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));
        keywordRepository.save(new Keyword("키워드3"));

        // when, then
        assertDoesNotThrow(() -> randomKeywordGenerator.generate());
    }
}
