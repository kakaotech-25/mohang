package moheng.keyword.domain;

import static moheng.fixture.KeywordFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import moheng.config.slice.RepositoryTestConfig;
import moheng.keyword.domain.repository.KeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class KeywordRepositoryTest extends RepositoryTestConfig {
    @Autowired
    private KeywordRepository keywordRepository;

    @DisplayName("키워드 이름 리스트를 찾는다.")
    @Test
    void 키워드_이름_리스트를_찾는다() {
        // given
        Keyword 키워드1 = keywordRepository.save(키워드1_생성());
        Keyword 키워드2 = keywordRepository.save(키워드2_생성());
        Keyword 키워드3 = keywordRepository.save(키워드3_생성());

        // when
        List<String> keywordNames = keywordRepository.findNamesByIds(List.of(키워드1.getId(), 키워드2.getId(), 키워드3.getId()));

        // then
        assertThat(keywordNames.size()).isEqualTo(3);
    }
}
