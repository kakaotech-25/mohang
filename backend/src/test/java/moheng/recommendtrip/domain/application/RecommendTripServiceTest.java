package moheng.recommendtrip.domain.application;

import static moheng.fixture.MemberFixtures.*;
import static moheng.fixture.MemberFixtures.하온_기존;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.ServiceTestConfig;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.application.RecommendTripService;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.application.TripService;
import moheng.trip.domain.Trip;
import moheng.trip.repository.TripRepository;
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
        Member member = memberRepository.save(하온_기존());
        Trip trip = tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "이미지 경로"));

        // when, then
        assertDoesNotThrow(() -> recommendTripService.saveByRank(trip, member, 1L));
    }

    @DisplayName("추천 여행지 정보를 저장한다.")
    @Test
    void 추천_여행지_정보를_저장한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip trip = tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "이미지 경로"));

        // when, then
        assertDoesNotThrow(() -> recommendTripService.createRecommendTrip(member.getId(), new RecommendTripCreateRequest(trip.getId())));
    }
}
