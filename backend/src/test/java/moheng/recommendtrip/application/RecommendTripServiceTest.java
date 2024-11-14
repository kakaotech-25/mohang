package moheng.recommendtrip.application;

import static moheng.fixture.RecommendTripFixture.*;
import static moheng.fixture.TripFixture.*;
import static moheng.fixture.MemberFixtures.하온_기존;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.trip.domain.Trip;
import moheng.trip.exception.NoExistTripException;
import moheng.trip.domain.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RecommendTripServiceTest extends ServiceTestConfig {
    @Autowired
    private RecommendTripService recommendTripService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @DisplayName("순위와 함께 추천 여행지 정보를 저장한다.")
    @Test
    void 순위와_함께_추천_여행지_정보를_저장한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성());

        // when, then
        assertDoesNotThrow(() -> recommendTripService.saveByRank(여행지1, 하온, 1L));
    }

    @DisplayName("추천 여행지 정보를 저장한다.")
    @Test
    void 추천_여행지_정보를_저장한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성());

        // when, then
        assertDoesNotThrow(() -> recommendTripService.createRecommendTrip(하온.getId(), 선호_여행지_생성_요청(여행지1.getId())));
    }

    @DisplayName("존재하지 않는 여행지의 추천 정보를 저장하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지의_추천_정보를_저장하면_예외가_발생한다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());

        // when, then
        assertThatThrownBy(() ->
                recommendTripService.createRecommendTrip(하온.getId(), 유효하지_않은_선호_여행지_생성_요청())
        ).isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("존재하지 않는 유저에 대한 추천 정보를 저장하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_유자에_대한_추천_정보를_저장하면_예외가_발생한다() {
        // given
        long 존재하지_않는_멤버_ID = -1L;
        Trip 여행지1 = tripRepository.save(여행지1_생성());

        // when, then
        assertThatThrownBy(() ->
                recommendTripService.createRecommendTrip(존재하지_않는_멤버_ID, 선호_여행지_생성_요청(여행지1.getId()))
        ).isInstanceOf(NoExistMemberException.class);
    }
}
