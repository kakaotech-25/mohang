package moheng.recommendtrip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static moheng.fixture.MemberFixtures.*;
import moheng.config.slice.RepositoryTestConfig;
import moheng.config.TestConfig;
import moheng.keyword.dto.RecommendTripResponse;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import moheng.trip.application.TripService;
import moheng.trip.domain.Trip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = TestConfig.class)
public class RecommendTripRepositoryTest extends RepositoryTestConfig {
    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TripService tripService;

    @DisplayName("멤버의 선호 여행지 contentId 와 방문횟수를 찾는다.")
    @Test
    void 멤버의_선호_여행지_contentId_와_방문횟수를_찾는다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);
        tripService.save(new Trip("여행지", "장소명", 1L, "설명", "이미지 경로"));
        Trip trip = tripService.findByContentId(1L);
        recommendTripRepository.save(new RecommendTrip(trip, member));

        // when
        List<RecommendTripResponse> expected = recommendTripRepository.findVisitedCountAndTripContentIdByMemberId(member.getId());

        // then
        assertThat(expected).hasSize(1);
    }

    @DisplayName("멤버의 선호 여행지를 찾는다.")
    @Test
    void 멤버의_선호_여행지를_찾는다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);
        tripService.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로"));
        tripService.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로"));
        Trip trip = tripService.findByContentId(1L);
        recommendTripRepository.save(new RecommendTrip(trip, member));
        recommendTripRepository.save(new RecommendTrip(trip, member));

        // when, then
        assertThat(recommendTripRepository.findAllByMemberId(member.getId())).hasSize(2);
    }
}
