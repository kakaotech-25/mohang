package moheng.recommendtrip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static moheng.fixture.MemberFixtures.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.RepositoryTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RecommendTripRepositoryTest extends RepositoryTestConfig {
    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @DisplayName("멤버의 선호 여행지를 찾는다.")
    @Test
    void 멤버의_선호_여행지를_찾는다() {
        // given
        memberRepository.save(하온_기존());
        Member member = memberRepository.findByEmail(하온_이메일).orElseThrow();
        tripRepository.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로"));
        tripRepository.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로"));
        Trip trip = tripRepository.findByContentId(1L).get();
        recommendTripRepository.save(new RecommendTrip(trip, member));
        recommendTripRepository.save(new RecommendTrip(trip, member));

        // when, then
        assertThat(recommendTripRepository.findAllByMemberId(member.getId())).hasSize(2);
    }

    @DisplayName("멤버의 최대 10개 선호 여행지를 찾는다.")
    @Test
    void 멤버의_최대_10개_선호_여행지를_찾는다() {
        // given
        memberRepository.save(하온_기존());
        Member member = memberRepository.findByEmail(하온_이메일).orElseThrow();
        tripRepository.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로"));
        tripRepository.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로"));
        Trip trip = tripRepository.findByContentId(1L).get();
        recommendTripRepository.save(new RecommendTrip(trip, member));
        recommendTripRepository.save(new RecommendTrip(trip, member));

        // when, then
        assertThat(recommendTripRepository.findByMemberOrderByRankingDesc(member)).hasSize(2);
    }

    @DisplayName("모든 선호 여행지의 rank 를 1씩 감소시킨다.")
    @Test
    void 모든_선호_여행지의_rank_를_1씩_감소시킨다() {
        // given
        memberRepository.save(하온_기존());
        Member member = memberRepository.findByEmail(하온_이메일).orElseThrow();

        tripRepository.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로"));
        tripRepository.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로"));
        tripRepository.save(new Trip("여행지3", "장소명2", 3L, "설명3", "이미지 경로"));
        tripRepository.save(new Trip("여행지4", "장소명2", 4L, "설명4", "이미지 경로"));

        Trip trip1 = tripRepository.findByContentId(1L).orElseThrow();
        Trip trip2 = tripRepository.findByContentId(2L).orElseThrow();
        Trip trip3 = tripRepository.findByContentId(3L).orElseThrow();
        Trip trip4 = tripRepository.findByContentId(4L).orElseThrow();

        RecommendTrip recommendTrip1 = recommendTripRepository.save(new RecommendTrip(trip1, member, 1L));
        RecommendTrip recommendTrip2 = recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        RecommendTrip recommendTrip3 = recommendTripRepository.save(new RecommendTrip(trip3, member, 3L));
        RecommendTrip recommendTrip4 = recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));

        // when
        List<RecommendTrip> recommendTrips = recommendTripRepository.findAllByMemberId(member.getId());
        recommendTripRepository.bulkDownRank(recommendTrips);

        // then
        assertAll(() -> {
            assertThat(recommendTripRepository.findById(recommendTrip1.getId()).get().getRanking()).isEqualTo(1L);
            assertThat(recommendTripRepository.findById(recommendTrip2.getId()).get().getRanking()).isEqualTo(2L);
            assertThat(recommendTripRepository.findById(recommendTrip3.getId()).get().getRanking()).isEqualTo(3L);
            assertThat(recommendTripRepository.findById(recommendTrip4.getId()).get().getRanking()).isEqualTo(4L);
        });
    }

}
