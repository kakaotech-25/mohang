package moheng.trip.application;

import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.recommendstrategy.ReplaceRecommendTripStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static moheng.fixture.MemberFixtures.하온_기존;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ReplaceRecommendTripStrategyTest extends ServiceTestConfig {
    @Autowired
    private ReplaceRecommendTripStrategy replaceRecommendTripStrategy;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripService tripService;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @DisplayName("교체 전략은 기존의 rank 가 10인 선호 여행지를 제거하고 새로운 선호 여행지를 생성한다.")
    @Test
    void 기존의_rank_가_10인_선호_여행지를_제거하고_새로운_선호_여행지를_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        tripService.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        tripService.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        tripService.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        tripService.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        tripService.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        tripService.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L); Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip2, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 10L));

        // when
        Trip trip = tripService.findById(10L);
        List<RecommendTrip> recommendTrips = recommendTripRepository.findAllByMemberId(member.getId());
        replaceRecommendTripStrategy.execute(trip, member, recommendTrips);

        // then
        assertAll(() -> {
            assertThat(recommendTripRepository.findById(11L).get().getRank()).isEqualTo(10L);
        });
    }

    @DisplayName("교체 전략은 최근 클릭한 여행지가 10개라면 기존의 rank를 1씩 감소시키고, rank가 10인 새로운 선호 여행지를 생성한다.")
    @Test
    void 교체_전략은_최근_클릭한_여행지가_10개라면_기존의_rank를_1씩_감소시키고_rank가_10인_새로운_선호_여행지를_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        tripService.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        tripService.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        tripService.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        tripService.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        tripService.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        tripService.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L); Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip2, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip3, member, 10L));

        // when
        Trip trip = tripService.findById(10L);
        tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());

        // then
        assertAll(() -> {
            assertThat(recommendTripRepository.findById(11L).get().getRank()).isEqualTo(10L);
        });
    }
}
