package moheng.trip.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.liveinformation.domain.LiveInformation;
import moheng.liveinformation.domain.repository.LiveInformationRepository;
import moheng.liveinformation.domain.TripLiveInformation;
import moheng.liveinformation.domain.repository.TripLiveInformationRepository;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.trip.domain.MemberTrip;
import moheng.trip.domain.repository.MemberTripRepository;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.dto.FindTripWithSimilarTripsResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripCreateRequest;
import moheng.trip.exception.NoExistRecommendTripException;
import moheng.trip.exception.NoExistRecommendTripStrategyException;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class TripServiceTest extends ServiceTestConfig {
    private static final long HIGHEST_PRIORITY_RANK = 1L;
    private static final long LOWEST_PRIORITY_RANK = 10L;

    @Autowired
    private TripService tripService;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTripRepository memberTripRepository;

    @Autowired
    private TripKeywordRepository tripKeywordRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private LiveInformationRepository liveInformationRepository;

    @Autowired
    private TripLiveInformationRepository tripLiveInformationRepository;

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

        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));
        keywordRepository.save(new Keyword("키워드3"));

        tripKeywordRepository.save(new TripKeyword(tripService.findById(1L), keywordRepository.findById(1L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(2L), keywordRepository.findById(2L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(3L), keywordRepository.findById(3L).get()));

        // when, then
        assertThat(tripService.findTop30OrderByVisitedCountDesc()
                .getFindTripResponses().size()).isEqualTo(3);
    }

    @DisplayName("방문 수 기준 상위 여행지들을 내림차순으로 조회한다.")
    @ParameterizedTest
    @MethodSource("여행지를_찾는다")
    void 방문_수_기준_상위_여행지들을_내림차순으로_조회한다(List<Trip> trips, List<String> expectedTripNames) {
        // given
        for (Trip trip : trips) {
            tripService.save(trip);
        }

        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));
        keywordRepository.save(new Keyword("키워드3"));

        tripKeywordRepository.save(new TripKeyword(tripService.findById(1L), keywordRepository.findById(1L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(2L), keywordRepository.findById(2L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(3L), keywordRepository.findById(3L).get()));

        // when
        FindTripsResponse response = tripService.findTop30OrderByVisitedCountDesc();

        // then
        assertAll(() -> {
            assertThat(response.getFindTripResponses().size()).isEqualTo(trips.size());
            for (int i = 0; i < expectedTripNames.size(); i++) {
                assertThat(response.getFindTripResponses().get(i).getName()).isEqualTo(expectedTripNames.get(i));
            }
        });
    }

    static Stream<Arguments> 여행지를_찾는다() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Trip("여행지1", "서울", 1L, "설명", "https://image.png", 0L),
                                new Trip("여행지2", "서울", 2L, "설명", "https://image.png", 2L),
                                new Trip("여행지3", "서울", 3L, "설명", "https://image.png", 1L)
                        ),
                        List.of("여행지2", "여행지3", "여행지1")
                )
        );
    }

    @DisplayName("현재 여행지를 비슷한 여행지 10개와 함께 조회한다.")
    @Test
    void 현재_여행지를_비슷한_여행지와_함께_조회한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));
        recommendTripRepository.save(new RecommendTrip(currentTrip, member, 1L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        FindTripWithSimilarTripsResponse response = tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());

        // then
        assertThat(response.getFindTripResponse()).isNotNull();
    }

    @DisplayName("비슷한 여행지를 정확히 10개를 찾는다.")
    @Test
    void 비슷한_여행지를_정확히_10개를_찾는다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));
        recommendTripRepository.save(new RecommendTrip(currentTrip, member, 1L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        FindTripWithSimilarTripsResponse response = tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());

        // then
        assertThat(response.getSimilarTripResponses().getFindTripResponses()).hasSize(10);
    }


    @DisplayName("현재 여행지를 조회하면 모든 유저에 대한 총 방문 횟수가 증가한다.")
    @Test
    void 현재_여행지를_조회하면_모든_유저에_대한_총_방문_횟수가_증가한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));
        recommendTripRepository.save(new RecommendTrip(currentTrip, member, 1L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());
        long expected = tripService.findById(currentTrip.getId()).getVisitedCount();

        // then
        assertThat(expected).isEqualTo(1L);
    }

    @DisplayName("현재 여행지를 조회하면 각 유저에 대한 방문 횟수가 증가한다.")
    @Test
    void 현재_여행지를_조회하면_각_유저에_대한_방문_횟수가_증가한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));
        recommendTripRepository.save(new RecommendTrip(currentTrip, member, 1L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());

        // then
        assertThat(recommendTripRepository.findById(1L).get().getRanking()).isEqualTo(1L);
    }

    @DisplayName("존재하지 않는 회원이 여행지를 조회하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_회원이_여행지를_조회하면_예외가_발생한다() {
        // given
        long invalidMemberId = -1L;
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));
        recommendTripRepository.save(new RecommendTrip(currentTrip, member, 1L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        // when, then
        assertThatThrownBy(() -> tripService.findWithSimilarOtherTrips(currentTrip.getId(), invalidMemberId))
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


    @DisplayName("최근 클릭한 여행지가 10개 미만이라면 가장 높은 rank 에 1을 더한 선호 여행지 정보를 생성한다.")
    @Test
    void 최근_클릭한_여행지가_10개_미만이라면_가장_높은_rank_에_1을_더한_선호_여행지_정보를_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));

        // when
        tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());

        // then
        RecommendTrip actual = recommendTripRepository.findById(7L).get();
        assertThat(actual.getRanking()).isEqualTo(7L);
    }

    @DisplayName("최근 클릭한 여행지가 10개라면 기존의 rank를 1씩 감소시키고, rank가 10인 새로운 선호 여행지를 생성한다.")
    @Test
    void 최근_클릭한_여행지가_10개라면_기존의_rank를_1씩_감소시키고_rank가_10인_새로운_선호_여행지를_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());

        // then
        assertAll(() -> {
            assertThat(recommendTripRepository.findById(11L).get().getRanking()).isEqualTo(10L);
        });
    }

    @DisplayName("현재 여행지를 조회하고 선호 여행지가 10개라면 랭크가 1 감소한다.")
    @Test
    void 현재_여행지를_조회하고_선호_여행지가_10개라면_랭크가_1_감소한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());

        // then
        assertAll(() -> {
            for(long id=2; id<=11; id++) {
                assertThat(recommendTripRepository.findById(id).get().getRanking()).isEqualTo(id-1);
            }
        });
    }

    @DisplayName("가장 높았던 랭크를 보유한 선호 여행지를 제거한다.")
    @Test
    void 가장_높았던_랭크를_보유한_선호_여행지를_제거한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());

        // then
        assertAll(() -> {
            assertThat(recommendTripRepository.findAllByMemberId(member.getId()).size()).isEqualTo(10);
            assertThat(recommendTripRepository.existsByMemberAndTrip(member, trip2)).isFalse();
            assertThat(recommendTripRepository.findById(2L).get().getRanking()).isEqualTo(HIGHEST_PRIORITY_RANK);
        });
    }

    @DisplayName("기존에 rank가 1등인 선호 여행지가 존재하지 않으면 예외가 발생한다.")
    @Test
    void 기존에_rank가_1등인_선호_여행지가_존재하지_않으면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 10L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 11L));

        // when, then
        assertThatThrownBy(() -> tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId()))
                .isInstanceOf(NoExistRecommendTripException.class);
    }

    @DisplayName("가장 낮은 우선순위를 가진 랭크가 10인 신규 선호 여행지를 생성한다.")
    @Test
    void 가장_낮은_우선순위를_가진_랭크가_10인_신규_선호_여행지를_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());
        RecommendTrip recommendTrip = recommendTripRepository.findById(11L).get();

        // then
        assertThat(recommendTrip.getRanking()).isEqualTo(LOWEST_PRIORITY_RANK);
    }


    @DisplayName("처음 방문한 여행지라면 방문 횟수는 1이 된다.")
    @Test
    void 처음_방문한_여행지라면_방문_횟수는_1이_된다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());

        // then
        assertThat(memberTripRepository.findByMemberAndTrip(member, currentTrip).getVisitedCount()).isEqualTo(1L);
    }

    @DisplayName("이전에 방문했던 여행지라면 방문 횟수가 1이 증가한다.")
    @Test
    void 이전에_방문했던_여행지라면_방문_횟수가_1이_증가한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        Trip alreadyVisitedTrip = tripRepository.findById(10L).get();
        memberTripRepository.save(new MemberTrip(member, alreadyVisitedTrip, 100L));

        // when
        tripService.findWithSimilarOtherTrips(alreadyVisitedTrip.getId(), member.getId());

        // then
        assertThat(memberTripRepository.findByMemberAndTrip(member, alreadyVisitedTrip).getVisitedCount()).isEqualTo(101L);
    }

    @DisplayName("현재 방문한 여행지가 최근 클릭한 선호 여행지 리스트에 포함된다면 선호 여행지들의 랭크를 수정하지 않는다.")
    @Test
    void 현재_방문한_여행지가_최근_클릭한_선호_여행지_리스트에_포함된다면_선호_여행지들의_랭크를_수정하지_않는다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        Trip alreadyVisitedTrip = tripRepository.findById(10L).get();

        // when
        tripService.findWithSimilarOtherTrips(alreadyVisitedTrip.getId(), member.getId());

        // then
        assertAll(() -> {
            for(long id=1; id<=10; id++) {
                assertThat(recommendTripRepository.findById(id).get().getRanking()).isEqualTo(id);
            }
        });
    }

    @DisplayName("현재 여행지를 조회할 때 멤버의 선호 여행지 정보가 아예 없다면 선호 여행지 저장 전략이 수행되지 않고 예외가 발생한다.")
    @Test
    void 현재_여행지를_조회할_때_멤버의_선호_여행지_정보가_아에_없다면_선호_여행지_저장_전략이_수행되지_않고_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));

        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        // when
        assertThatThrownBy(() -> tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId()))
                .isInstanceOf(NoExistRecommendTripStrategyException.class);
    }

    @DisplayName("멤버의 여행지를 생성한다")
    @Test
    void 멤버의_여행지를_생성한다() {
        // given
        Trip trip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Member member = memberRepository.save(하온_기존());

        // when, then
        assertDoesNotThrow(() -> tripService.createMemberTrip(member.getId(), trip.getId()));
    }

    @DisplayName("존재하지 않는 멤버에 대한 여행지를 생성하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버에_대한_여행지를_생성하면_예외가_발생한다() {
        // given
        Trip trip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));

        // when, then
        assertThatThrownBy(() -> tripService.createMemberTrip(-1L, trip.getId()))
                .isInstanceOf(NoExistMemberException.class);
    }

    @DisplayName("존재하지 않는 여행지로 멤버 여행지를 생성하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지로_멤버_여행지를_생성하면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());

        // when, then
        assertThatThrownBy(() -> tripService.createMemberTrip(member.getId(), -1L))
                .isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("동시간대에 100명의 유저가 같은 여행지를 조회할 경우, 방문 수는 정확히 100으로 증가한다.")
    @Test
    void 동시간대에_100명의_유저가_같은_여행지를_조회할_경우_방문_수는_정확히_100으로_증가한다() throws InterruptedException {
        // given
        Member member = memberRepository.save(하온_기존());
        Trip currentTrip = tripRepository.save(new Trip("여행지1", "서울", 1L, "설명1", "https://image.png", 0L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "서울", 2L, "설명2", "https://image.png", 0L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "서울", 3L, "설명3", "https://image.png", 0L));
        Trip trip4 = tripRepository.save(new Trip("여행지4", "서울", 4L, "설명4", "https://image.png", 0L));
        Trip trip5 = tripRepository.save(new Trip("여행지5", "서울", 5L, "설명5", "https://image.png", 0L));
        Trip trip6 = tripRepository.save(new Trip("여행지6", "서울", 6L, "설명6", "https://image.png", 0L));
        Trip trip7 = tripRepository.save(new Trip("여행지7", "서울", 7L, "설명7", "https://image.png", 0L));
        Trip trip8 = tripRepository.save(new Trip("여행지8", "서울", 8L, "설명8", "https://image.png", 0L));
        Trip trip9 = tripRepository.save(new Trip("여행지9", "서울", 9L, "설명9", "https://image.png", 0L));
        Trip trip10 = tripRepository.save(new Trip("여행지10", "서울", 10L, "설명10", "https://image.png", 0L));
        Trip trip11 = tripRepository.save(new Trip("여행지11", "서울", 11L, "설명10", "https://image.png", 0L));
        recommendTripRepository.save(new RecommendTrip(currentTrip, member, 1L));
        LiveInformation liveInformation = liveInformationRepository.save(new LiveInformation("생활정보1"));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, currentTrip));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip2)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip3));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip4)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip5));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip6)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip7));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip8)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip9));
        tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip10)); tripLiveInformationRepository.save(new TripLiveInformation(liveInformation, trip11));

        recommendTripRepository.save(new RecommendTrip(trip2, member, 1L));
        recommendTripRepository.save(new RecommendTrip(trip3, member, 2L));
        recommendTripRepository.save(new RecommendTrip(trip4, member, 3L));
        recommendTripRepository.save(new RecommendTrip(trip5, member, 4L));
        recommendTripRepository.save(new RecommendTrip(trip6, member, 5L));
        recommendTripRepository.save(new RecommendTrip(trip7, member, 6L));
        recommendTripRepository.save(new RecommendTrip(trip8, member, 7L));
        recommendTripRepository.save(new RecommendTrip(trip9, member, 8L));
        recommendTripRepository.save(new RecommendTrip(trip10, member, 9L));
        recommendTripRepository.save(new RecommendTrip(trip11, member, 10L));

        // when
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    tripService.findWithSimilarOtherTrips(currentTrip.getId(), member.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Trip updatedTrip = tripRepository.findById(currentTrip.getId()).orElseThrow();
        System.out.println(updatedTrip.getVisitedCount());
        assertThat(updatedTrip.getVisitedCount()).isEqualTo(100);
    }
}
