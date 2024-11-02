package moheng.recommendtrip.domain;

import static moheng.fixture.TripFixture.*;
import static moheng.fixture.TripFixture.여행지4_생성;
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
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        recommendTripRepository.save(new RecommendTrip(여행지1, 하온));
        recommendTripRepository.save(new RecommendTrip(여행지2, 하온));

        // when, then
        assertThat(recommendTripRepository.findAllByMemberId(하온.getId())).hasSize(2);
    }

    @DisplayName("멤버의 최대 10개 선호 여행지를 찾는다.")
    @Test
    void 멤버의_최대_10개_선호_여행지를_찾는다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        recommendTripRepository.save(new RecommendTrip(여행지1, 하온));
        recommendTripRepository.save(new RecommendTrip(여행지1, 하온));

        // when, then
        assertThat(recommendTripRepository.findByMemberOrderByRankingDesc(하온)).hasSize(2);
    }

    @DisplayName("모든 선호 여행지의 rank 를 1씩 감소시킨다.")
    @Test
    void 모든_선호_여행지의_rank_를_1씩_감소시킨다() {
        // given
        Member 하온 = memberRepository.save(하온_기존());

        Trip 여행지1 = tripRepository.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로"));
        Trip 여행지2 = tripRepository.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로"));
        Trip 여행지3 = tripRepository.save(new Trip("여행지3", "장소명2", 3L, "설명3", "이미지 경로"));
        Trip 여행지4 = tripRepository.save(new Trip("여행지4", "장소명2", 4L, "설명4", "이미지 경로"));

        RecommendTrip recommendTrip1 = recommendTripRepository.save(new RecommendTrip(여행지1, 하온, 1L));
        RecommendTrip recommendTrip2 = recommendTripRepository.save(new RecommendTrip(여행지2, 하온, 2L));
        RecommendTrip recommendTrip3 = recommendTripRepository.save(new RecommendTrip(여행지3, 하온, 3L));
        RecommendTrip recommendTrip4 = recommendTripRepository.save(new RecommendTrip(여행지4, 하온, 4L));

        // when
        List<RecommendTrip> recommendTrips = recommendTripRepository.findAllByMemberId(하온.getId());
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
