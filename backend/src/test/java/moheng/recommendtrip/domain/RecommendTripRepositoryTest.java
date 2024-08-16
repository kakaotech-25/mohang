package moheng.recommendtrip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static moheng.fixture.MemberFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @DisplayName("멤버의 최대 10개 선호 여행지를 찾는다.")
    @Test
    void 멤버의_최대_10개_선호_여행지를_찾는다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);
        tripService.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로"));
        tripService.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로"));
        Trip trip = tripService.findByContentId(1L);
        recommendTripRepository.save(new RecommendTrip(trip, member));
        recommendTripRepository.save(new RecommendTrip(trip, member));

        // when, then
        assertThat(recommendTripRepository.findTop10ByMember(member)).hasSize(2);
    }

    @DisplayName("모든 선호 여행지의 rank 를 1씩 감소시킨다.")
    @Test
    void 모든_선호_여행지의_rank_를_1씩_감소시킨다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);
        tripService.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로"));
        tripService.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로"));
        tripService.save(new Trip("여행지3", "장소명2", 3L, "설명3", "이미지 경로"));
        tripService.save(new Trip("여행지4", "장소명2", 4L, "설명4", "이미지 경로"));
        Trip trip1 = tripService.findByContentId(1L); Trip trip2 = tripService.findByContentId(2L);
        Trip trip3 = tripService.findByContentId(3L); Trip trip4 = tripService.findByContentId(4L);

        recommendTripRepository.save(new RecommendTrip(trip1, member, 1L)); recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L)); recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));

        // when
        List<RecommendTrip> recommendTrips = recommendTripRepository.findAllByMemberId(member.getId());
        recommendTripRepository.bulkDownRank(recommendTrips);

        // then
        assertAll(() -> {
            for(long id=1; id<=4; id++) {
                assertThat(recommendTripRepository.findById(id).get().getRank()).isEqualTo(id-1);
            }
        });
    }
}
