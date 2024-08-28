package moheng.trip.application;

import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.recommendstrategy.AppendRecommendTripStrategy;
import moheng.trip.exception.NoExistRecommendTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static moheng.fixture.MemberFixtures.하온_기존;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AppendRecommendTripStrategyTest extends ServiceTestConfig {
    private static final long MAX_SIZE = 10L;
    private static final long MIN_SIZE = 5L;

    @Autowired
    private AppendRecommendTripStrategy appendRecommendTripStrategy;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripService tripService;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @DisplayName("선호 여행지 추가 전략은 최근 클릭한 여행지가 10개 미만이라면 가장 높은 rank 에 1을 더한 선호 여행지 정보를 생성한다.")
    @Test
    public void 선호_여행지_추가_전략은_가장_높은_rank_에_1을_더한_선호_여행지_정보를_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L);
        Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L);
        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L));

        List<RecommendTrip> recommendTrips = recommendTripRepository.findAllByMemberId(member.getId());

        // when
        Trip trip4 = tripService.findById(4L);
        appendRecommendTripStrategy.execute(trip4, member, recommendTrips);

        // then
        RecommendTrip actual = recommendTripRepository.findById(trip4.getId()).get();

        assertAll(() -> {
            assertThat(recommendTripRepository.findAllByMemberId(member.getId()).size()).isEqualTo(4L);
            assertThat(actual.getRank()).isEqualTo(4L);
        });
    }

    @DisplayName("선호 여행지 추가 전략은 현재 선호 여행지 개수가 MAX SIZE 보다 작고 MIN SIZE 보다 클 때 수행된다.")
    @Test
    void 선호_여행지_추가_전략은_MAX_SIZE_보다_작을_때_수행된다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        tripService.save(new Trip("여행지5", "서울", 5L, "설명4", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L);
        Trip trip2 = tripService.findById(2L);
        Trip trip3 = tripService.findById(3L);
        Trip trip4 = tripService.findById(4L);
        Trip trip5 = tripService.findById(5L);
        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L));

        long recommendSize = recommendTripRepository.findAllByMemberId(member.getId()).size();

        assertThat(appendRecommendTripStrategy.isMatch(recommendSize, MAX_SIZE, MIN_SIZE)).isTrue();
    }

    @DisplayName("멤버의 선호 여행지가 비어있으면 선호 여행지 추가 전략에 예외가 발생한다.")
    @Test
    void 멤버의_선호_여행지가_비어있으면_선호_여행지_추가_전략에_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip1 = tripService.findById(1L);

        // when, then
        assertThatThrownBy(() ->appendRecommendTripStrategy.execute(trip1, member, new ArrayList<>()))
                .isInstanceOf(NoExistRecommendTripException.class);
    }
}
