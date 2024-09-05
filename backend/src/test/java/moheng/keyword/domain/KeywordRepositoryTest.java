package moheng.keyword.domain;

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
        List<Keyword> keywords = keywordRepository.saveAll(List.of(new Keyword("키워드1"), new Keyword("키워드2"), new Keyword("키워3")));

        // when
        List<String> keywordNames = keywordRepository.findNamesByIds(List.of(keywords.get(0).getId(), keywords.get(1).getId(), keywords.get(2).getId()));

        // then
        assertThat(keywordNames.size()).isEqualTo(3);
    }
}
