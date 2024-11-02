package moheng.keyword.domain;

import static moheng.fixture.TripFixture.*;
import static moheng.fixture.KeywordFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.keyword.domain.statistics.TripsByVisitedCountFinder;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TripsByVisitedCountFinderTest extends ServiceTestConfig {
    @Autowired
    private TripsByVisitedCountFinder tripsByVisitedCountFinder;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private TripKeywordRepository tripKeywordRepository;

    @DisplayName("방문 수 기준 상위 여행지를 찾는다.")
    @Test
    public void 방문_수_기준_상위_여행지를_찾는다() {
        // given
        Trip 여행지1_1등 = tripRepository.save(여행지1_생성_방문수_1등());
        Trip 여행지2_3등 = tripRepository.save(여행지2_생성_방문수_3등());
        Trip 여행지3_2등 = tripRepository.save(여행지3_생성_방문수_2등());
        Trip 여행지4_4등 = tripRepository.save(여행지4_생성_방문수_4등());

        Keyword 키워드1 = keywordRepository.save(키워드1_생성());

        List<TripKeyword> tripKeywords = new ArrayList<>();
        tripKeywords.add(tripKeywordRepository.save(new TripKeyword(여행지1_1등, 키워드1)));
        tripKeywords.add(tripKeywordRepository.save(new TripKeyword(여행지2_3등, 키워드1)));
        tripKeywords.add(tripKeywordRepository.save(new TripKeyword(여행지3_2등, 키워드1)));
        tripKeywords.add(tripKeywordRepository.save(new TripKeyword(여행지4_4등, 키워드1)));

        // when
        List<Trip> actual = tripsByVisitedCountFinder.findTripsWithVisitedCount(tripKeywords);

        // then
        assertAll(() -> {
            assertThat(actual.size()).isEqualTo(4);
            assertThat(actual.get(0).getId()).isEqualTo(여행지1_1등.getId());
            assertThat(actual.get(1).getId()).isEqualTo(여행지3_2등.getId());
            assertThat(actual.get(2).getId()).isEqualTo(여행지2_3등.getId());
            assertThat(actual.get(3).getId()).isEqualTo(여행지4_4등.getId());
        });
    }
}
