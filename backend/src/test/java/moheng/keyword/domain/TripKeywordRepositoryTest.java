package moheng.keyword.domain;

import static moheng.fixture.KeywordFixture.키워드1_생성;
import static moheng.fixture.TripFixture.*;
import static moheng.fixture.KeywordFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import moheng.config.slice.RepositoryTestConfig;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TripKeywordRepositoryTest extends RepositoryTestConfig {
    @Autowired
    private TripKeywordRepository tripKeywordRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @DisplayName("키워드에 해당하는 여행지 키워드들을 찾는다.")
    @Test
    void 키워드에_해당하는_여행지_키워드들을_찾는다 () {
        // given
        Trip 여행지1 = tripRepository.save(여행지1_생성());
        Keyword 키워드1 = keywordRepository.save(키워드1_생성());
        Keyword 키워드2 = keywordRepository.save(키워드2_생성());

        tripKeywordRepository.save(new TripKeyword(여행지1, 키워드1));
        tripKeywordRepository.save(new TripKeyword(여행지1, 키워드2));

        // when, then
        assertThat(tripKeywordRepository.findTripKeywordsByKeywordIds(List.of(키워드1.getId(), 키워드2.getId())).size()).isEqualTo(2);
    }

    @DisplayName("키워드에 해당하는 여행지를 찾는다.")
    @Test
    void 키워드에_해당하는_여행지를_찾는다() {
        // given
        Trip 여행지1 = tripRepository.save(여행지1_생성());
        Keyword 키워드1 = keywordRepository.save(키워드1_생성());
        tripKeywordRepository.save(new TripKeyword(여행지1, 키워드1));

        // when, then
        assertThat(tripKeywordRepository.findTop30ByKeywordId(키워드1.getId()).size()).isEqualTo(1);
    }

    @DisplayName("ID 값이 가장 작은 키워드를 찾는다.")
    @Test
    void ID_값익_가장_작은_키워드를_찾는다() {
        // given
        Keyword 가장_작은_ID_키워드 = keywordRepository.save(키워드1_생성());
        keywordRepository.save(키워드2_생성());

        // when, then
        assertThat(keywordRepository.findMinKeywordId()).isEqualTo(가장_작은_ID_키워드.getId());
    }

    @DisplayName("ID 값이 가장 큰 키워드를 찾는다.")
    @Test
    void ID_값익_가장_큰_키워드를_찾는다() {
        // given
        keywordRepository.save(키워드1_생성());
        keywordRepository.save(키워드2_생성());
        Keyword 가장_큰_ID_키워드 = keywordRepository.save(키워드3_생성());

        // when, then
        assertThat(keywordRepository.findMaxKeywordId()).isEqualTo(가장_큰_ID_키워드.getId());
    }
}
