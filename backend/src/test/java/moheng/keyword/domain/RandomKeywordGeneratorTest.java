package moheng.keyword.domain;

import static moheng.fixture.KeywordFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.random.RandomKeywordGenerator;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.exception.NoExistKeywordException;
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
        keywordRepository.save(키워드1_생성());
        keywordRepository.save(키워드2_생성());
        keywordRepository.save(키워드3_생성());

        // when, then
        assertDoesNotThrow(() -> randomKeywordGenerator.generate());
    }
}
