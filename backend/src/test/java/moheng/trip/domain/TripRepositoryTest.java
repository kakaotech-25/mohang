package moheng.trip.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.RepositoryTestConfig;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.LiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformation;
import moheng.liveinformation.domain.TripLiveInformationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TripRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private LiveInformationRepository liveInformationRepository;

    @Autowired
    private TripLiveInformationRepository tripLiveInformationRepository;

    @DisplayName("방문수 기준 최대 상위 30개의 여행지를 내림차순으로 찾는다.")
    @Test
    void 방문수_기준_최대_상위_30개의_여헹지를_찾는다() {
        tripRepository.save(new Trip("여행지1", "장소", 1L, "설명", "이미지", 1L));
        tripRepository.save(new Trip("여행지1", "장소", 4L, "설명", "이미지", 4L));
        tripRepository.save(new Trip("여행지1", "장소", 2L, "설명", "이미지", 2L));
        tripRepository.save(new Trip("여행지1", "장소", 3L, "설명", "이미지", 3L));

        List<Trip> trips = tripRepository.findTop30ByOrderByVisitedCountDesc();

        assertAll(() -> {
            long visitedCount = 4L;
            for(Trip trip : trips) {
                assertThat(trip.getVisitedCount()).isEqualTo(visitedCount);
                visitedCount--;
            }
        });
    }

    @DisplayName("contentId 에 매핑되는 여행지들을 찾는다.")
    @Test
    void contentId_에_매핑되는_여행지들을_찾는다() {
        // given
        tripRepository.save(new Trip("여행지1", "장소", 1L, "설명", "이미지", 1L));
        tripRepository.save(new Trip("여행지2", "장소", 2L, "설명", "이미지", 2L));
        tripRepository.save(new Trip("여행지3", "장소", 3L, "설명", "이미지", 3L));
        tripRepository.save(new Trip("여행지4", "장소", 4L, "설명", "이미지", 4L));
        List<Long> contentIds = List.of(1L, 2L, 3L, 4L);

        // when, then
        assertThat(tripRepository.findTripsByContentIds(contentIds)).hasSize(4);
    }

    @DisplayName("생활정보로 필터링된 여행지 리스트를 찾는다.")
    @Test
    void 생활정보로_필터링된_여행지_리스트를_찾는다() {
        // given
        Trip trip1 = tripRepository.save(new Trip("여행지1", "장소", 1L, "설명", "이미지", 1L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "장소", 2L, "설명", "이미지", 2L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "장소", 3L, "설명", "이미지", 3L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "장소", 4L, "설명", "이미지", 4L));
        List<Long> contentIds = List.of(1L, 2L, 3L, 4L);

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip1));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4));


        // when, then
        assertThat(tripRepository.findTripsByContentIds(contentIds)).hasSize(4);
    }
}
