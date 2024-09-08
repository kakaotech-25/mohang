package moheng.keyword.domain;

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
        Trip trip1 = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명", "https://image.png", 2L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명", "https://image.png", 3L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 3L, "설명", "https://image.png", 1L));

        Keyword keyword = keywordRepository.save(new Keyword("키워드1"));

        List<TripKeyword> tripKeywords = new ArrayList<>();
        tripKeywords.add(tripKeywordRepository.save(new TripKeyword(trip1, keyword)));
        tripKeywords.add(tripKeywordRepository.save(new TripKeyword(trip2, keyword)));
        tripKeywords.add(tripKeywordRepository.save(new TripKeyword(trip3, keyword)));
        tripKeywords.add(tripKeywordRepository.save(new TripKeyword(trip4, keyword)));

        // when
        List<Trip> actual = tripsByVisitedCountFinder.findTripsWithVisitedCount(tripKeywords);

        // then
        assertAll(() -> {
            assertThat(actual.size()).isEqualTo(4);
            assertThat(actual.get(0).getVisitedCount()).isEqualTo(3L);
            assertThat(actual.get(1).getVisitedCount()).isEqualTo(2L);
            assertThat(actual.get(2).getVisitedCount()).isEqualTo(1L);
            assertThat(actual.get(3).getVisitedCount()).isEqualTo(0L);
        });
    }
}
