package moheng.trip.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import groovy.transform.AutoImplement;
import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.application.RecommendTripService;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripWithSimilarTripsResponse;
import moheng.trip.dto.TripCreateRequest;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TripServiceTest extends ServiceTestConfig {
    @Autowired
    private TripService tripService;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("여행지를 생성한다.")
    @Test
    void 여행지를_생성한다() {
        // given
        assertDoesNotThrow(() -> tripService.createTrip(new TripCreateRequest("롯데월드2", "서울특별시 송파구2", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.2",
                "https://lotte-world-image2.png")));
    }

    @DisplayName("여행지 정보를 저장한다.")
    @Test
    void 여행지_정보를_저장한다() {
        // given
        assertDoesNotThrow(() -> tripService.save(new Trip("롯데월드2", "서울특별시 송파구2", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.2",
                "https://lotte-world-image2.png")));
    }

    @DisplayName("여행지 정보를 가져온다.")
    @Test
    void 여행지_정보를_가져온다() {
        // given
        tripService.save(new Trip("롯데월드", "서울특별시 송파구", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.",
                "https://lotte-world-image.png"));

        // when, then
        assertDoesNotThrow(() -> tripService.findById(1L));
    }

    @DisplayName("여행지 정보가 없으면 예외가 발생한다.")
    @Test
    void 여행지_정보가_없으면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> tripService.findById(-1L)).
                isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("contentId 로 여행지 정보를 가져온다.")
    @Test
    void contentId_로_여행지_정보를_가져온다() {
        // given
        tripService.save(new Trip("롯데월드", "서울특별시 송파구", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.",
                "https://lotte-world-image.png"));

        // when, then
        assertDoesNotThrow(() -> tripService.findByContentId(1000000L));
    }

    @DisplayName("contentId 에 해당하는 여행지 정보가 없으면 예외가 발생한다.")
    @Test
    void contentId_에_해당하는여행지_정보가_없으면_예외가_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> tripService.findByContentId(-1L)).
                isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("방문 수 기준 상위 여행지들을 조회한다.")
    @Test
    void 방문_수_기준_상위_여행지들을_조회한다() {
         // given
        tripService.save(new Trip("여행지1", "서울", 1L, "설명", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명", "https://image.png", 2L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명", "https://image.png", 1L));

        // when, then
        assertThat(tripService.findTop30OrderByVisitedCountDesc()
                .getFindTripResponses().size()).isEqualTo(3);
    }

    @DisplayName("방문 수 기준 상위 여행지들을 오름차순으로 조회한다.")
    @Test
    void 방문_수_기준_상위_여행지들을_오름차순으로_조회한다() {
        // given
        tripService.save(new Trip("여행지1", "서울", 1L, "설명", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명", "https://image.png", 2L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명", "https://image.png", 1L));

        // when, then
        assertAll(() -> {
            assertThat(tripService.findTop30OrderByVisitedCountDesc()
                    .getFindTripResponses().get(0).getName()).isEqualTo("여행지2");

            assertThat(tripService.findTop30OrderByVisitedCountDesc()
                    .getFindTripResponses().get(1).getName()).isEqualTo("여행지3");

            assertThat(tripService.findTop30OrderByVisitedCountDesc()
                    .getFindTripResponses().get(2).getName()).isEqualTo("여행지1");
        });
    }

    @DisplayName("현재 여행지를 비슷한 여행지와 함께 조회한다.")
    @Test
    void 현재_여행지를_비슷한_여행지와_함께_조회한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip = tripService.findById(1L);
        recommendTripRepository.save(new RecommendTrip(trip, member, 1L));

        // when
        FindTripWithSimilarTripsResponse response = tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());

        // then
        assertAll(() -> {
            assertThat(response.getFindTripResponse()).isNotNull();
            assertThat(response.getSimilarTripResponses().getFindTripResponses().size()).isEqualTo(3);
        });
    }


    @DisplayName("현재 여행지를 조회하면 방문 횟수가 증가한다.")
    @Test
    void 현재_여행지를_조회하면_방문_횟수가_증가한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip = tripService.findById(1L);
        recommendTripRepository.save(new RecommendTrip(trip, member, 1L));

        // when
        tripService.findWithSimilarOtherTrips(trip.getId(), member.getId());
        long expected = tripService.findById(1L).getId();

        // then
        assertThat(expected).isEqualTo(1L);
    }

    @DisplayName("동시간대에 여러 유저가 여행지를 조회하면 방문 횟수에 동시성 이슈가 발생한다.")
    @Test
    void 동시간대에_여러_유저가_여행지를_조회하면_방문_횟수에_동시성_이슈가_발생한다() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(100);

        // given
        long currentTripId = 4L;
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));

        // when
        for (long memberId = 0; memberId < 100; memberId++) {
            long finalMemberId = memberId;
            executorService.submit(() -> {
                try {
                    tripService.findWithSimilarOtherTrips(currentTripId, finalMemberId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Trip trip = tripService.findById(currentTripId);
        assertThat(trip.getVisitedCount()).isNotEqualTo(104L);
    }

    @DisplayName("존재하지 않는 회원이 여행지를 조회하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_회원이_여행지를_조회하면_예외가_발생한다() {
        // given
        long invalidMemberId = -1L;
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip = tripService.findById(1L);

        // when, then
        assertThatThrownBy(() -> tripService.findWithSimilarOtherTrips(trip.getId(), invalidMemberId))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("존재하지 않는 여행지를 찾으면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지를_찾으면_예외가_발생한다() {
        // given
        long invalidTripId = -1L;
        Member member = memberRepository.save(하온_기존());
        tripService.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        tripService.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        tripService.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        tripService.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));

        // when, then
        assertThatThrownBy(() -> tripService.findWithSimilarOtherTrips(invalidTripId, member.getId()))
                .isInstanceOf(NoExistTripException.class);
    }
}
