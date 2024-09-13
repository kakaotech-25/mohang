package moheng.trip.application;

import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.recommendstrategy.ReplaceRecommendTripStrategy;
import moheng.trip.exception.NoExistRecommendTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static moheng.fixture.MemberFixtures.하온_기존;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ReplaceRecommendTripStrategyTest extends ServiceTestConfig {
    private static final long MAX_SIZE = 10L;
    private static final long MIN_SIZE = 5L;

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
            assertThat(recommendTripRepository.findById(11L).get().getRanking()).isEqualTo(10L);
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
        List<RecommendTrip> recommendTrips = recommendTripRepository.findAllByMemberId(member.getId());
        replaceRecommendTripStrategy.execute(trip, member, recommendTrips);

        // then
        assertAll(() -> {
            assertThat(recommendTripRepository.findById(11L).get().getRanking()).isEqualTo(10L);
        });
    }

    @DisplayName("교체 전략은 기존에 rank가 1등인 선호 여행지가 존재하지 않으면 예외가 발생한다.")
    @Test
    void 교체_전략은_기존에_rank가_1등인_선호_여행지가_존재하지_않으면_예외가_발생한다() {
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
        tripService.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L); Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L); Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L); Trip trip6 = tripService.findById(6L);
        Trip trip7 = tripService.findById(7L); Trip trip8 = tripService.findById(8L);
        Trip trip9 = tripService.findById(9L); Trip trip10 = tripService.findById(10L);
        Trip trip11 = tripService.findById(11L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 0L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(trip10, member, 10L));

        // when
        Trip trip = tripService.findById(11L);
        List<RecommendTrip> recommendTrips = recommendTripRepository.findAllByMemberId(member.getId());

        // then
        assertThatThrownBy(() -> replaceRecommendTripStrategy.execute(trip, member, recommendTrips))
                .isInstanceOf(NoExistRecommendTripException.class);
    }

    @DisplayName("교체 전략은 현재 방문한 여행지가 최근 클릭한 선호 여행지 리스트에 포함된다면 선호 여행지들의 랭크를 수정하지 않는다.")
    @Test
    void 교체_전략은_현재_방문한_여행지가_최근_클릭한_선호_여행지_리스트에_포함된다면_선호_여행지들의_랭크를_수정하지_않는다() {
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
        Trip trip3 = tripService.findById(3L); Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L); Trip trip6 = tripService.findById(6L);
        Trip trip7 = tripService.findById(7L); Trip trip8 = tripService.findById(8L);
        Trip trip9 = tripService.findById(9L);
        Trip alreadyVisitedTrip = tripService.findById(10L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(alreadyVisitedTrip, member, 10L));

        // when
        List<RecommendTrip> recommendTrips = recommendTripRepository.findAllByMemberId(member.getId());
        replaceRecommendTripStrategy.execute(alreadyVisitedTrip, member, recommendTrips);

        // then
        assertAll(() -> {
            for(long id=1; id<=10; id++) {
                assertThat(recommendTripRepository.findById(id).get().getRanking()).isEqualTo(id);
            }
        });
    }

    @DisplayName("교체 전략은 가장 높은 우선순위 rank 값을 가진 선호 여행지가 없다면 예외가 발생한다")
    @Test
    void 교체_전략은_가장_높은_우선순위_rank_값을_가진_선호_여행지가_없다면_예외가_발생한다() {
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
        Trip trip3 = tripService.findById(3L); Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L); Trip trip6 = tripService.findById(6L);
        Trip trip7 = tripService.findById(7L); Trip trip8 = tripService.findById(8L);
        Trip trip9 = tripService.findById(9L);
        Trip alreadyVisitedTrip = tripService.findById(10L);

        recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(alreadyVisitedTrip, member, 10L));
        recommendTripRepository.save(new RecommendTrip(trip2, member, 11L));

        List<RecommendTrip> recommendTrips = recommendTripRepository.findAllByMemberId(member.getId());

        // when, then
        assertThatThrownBy(() -> replaceRecommendTripStrategy.execute(trip1, member, recommendTrips))
                .isInstanceOf(NoExistRecommendTripException.class);
    }

    @DisplayName("교체 전략은 현재 선호 여행지 개수가 MAX SIZE 와 같을 때 수행된다.")
    @Test
    void 교체_전략은_현재_선호_여행지_개수가_MAX_SIZE_와_같을_때_수행된다() {
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
        Trip trip3 = tripService.findById(3L); Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L); Trip trip6 = tripService.findById(6L);
        Trip trip7 = tripService.findById(7L); Trip trip8 = tripService.findById(8L);
        Trip trip9 = tripService.findById(9L);
        Trip alreadyVisitedTrip = tripService.findById(10L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L)); recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L)); recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L)); recommendTripRepository.save(new RecommendTrip(alreadyVisitedTrip, member, 10L));

        long recommendSize = recommendTripRepository.findAllByMemberId(member.getId()).size();

        assertThat(replaceRecommendTripStrategy.isMatch(recommendSize, MAX_SIZE, MIN_SIZE)).isTrue();
    }
}
