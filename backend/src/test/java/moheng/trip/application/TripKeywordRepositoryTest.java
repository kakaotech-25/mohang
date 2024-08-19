package moheng.trip.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.RepositoryTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
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

    @DisplayName("여행지들에 관련한 여행지 키워드들을 찾는다.")
    @Test
    void 여행지들에_관련한_여행지_키워드들을_찾는다() {
        // given
        Trip trip1 = tripRepository.save(new Trip("여행지", "장소", 1L, "설명", "이미지"));
        Trip trip2 = tripRepository.save(new Trip("여행지", "장소", 1L, "설명", "이미지"));
        Keyword keyword1 = keywordRepository.save(new Keyword("키워드1"));
        Keyword keyword2 = keywordRepository.save(new Keyword("키워드1"));
        tripKeywordRepository.save(new TripKeyword(trip1, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip2, keyword2));

        // when, then
        assertThat(tripKeywordRepository.findByTrips(List.of(trip1, trip2))).hasSize(2);
    }
}
