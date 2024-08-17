package moheng.keyword.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import moheng.config.slice.RepositoryTestConfig;
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

    @DisplayName("키워드에 해당하는 여행지 키워드들을 찾는다.")
    @Test
    void 키워드에_해당하는_여행지_키워드들을_찾는다 () {
        // Save the trip and keywords
        Trip savedTrip = tripRepository.save(new Trip("여행", "장소", 1L, "설명", "이미지"));
        Keyword savedKeyword1 = keywordRepository.save(new Keyword("키워드1"));
        Keyword savedKeyword2 = keywordRepository.save(new Keyword("키워드2"));

        Trip trip = tripRepository.findById(savedTrip.getId()).orElseThrow(() -> new IllegalStateException("Trip not found"));
        Keyword keyword1 = keywordRepository.findById(savedKeyword1.getId()).orElseThrow(() -> new IllegalStateException("Keyword1 not found"));
        Keyword keyword2 = keywordRepository.findById(savedKeyword2.getId()).orElseThrow(() -> new IllegalStateException("Keyword2 not found"));

        tripKeywordRepository.save(new TripKeyword(trip, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip, keyword2));

        assertThat(tripKeywordRepository.findTripKeywordsByKeywordIds(List.of(keyword1.getId(), keyword2.getId())).size()).isEqualTo(2);
    }

}