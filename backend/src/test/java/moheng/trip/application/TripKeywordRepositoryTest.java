package moheng.trip.application;

import static moheng.fixture.TripFixture.*;
import static moheng.fixture.RecommendTripFixture.*;
import static moheng.fixture.KeywordFixture.*;
import static org.assertj.core.api.Assertions.*;

import moheng.config.slice.RepositoryTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
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

    @DisplayName("여행지들에 관련한 여행지 키워드들을 찾는다.")
    @Test
    void 여행지들에_관련한_여행지_키워드들을_찾는다() {
        // given
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        Keyword 키워드1 = keywordRepository.save(키워드1_생성()); Keyword 키워드2 = keywordRepository.save(키워드2_생성());
        tripKeywordRepository.save(new TripKeyword(여행지1, 키워드1));
        tripKeywordRepository.save(new TripKeyword(여행지2, 키워드2));

        // when, then
        assertThat(tripKeywordRepository.findByTrips(List.of(여행지1, 여행지2))).hasSize(2);
    }
}
