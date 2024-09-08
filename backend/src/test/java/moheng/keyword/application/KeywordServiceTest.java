package moheng.keyword.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.keyword.dto.FindTripsWithRandomKeywordResponse;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.exception.NoExistKeywordException;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import moheng.recommendtrip.domain.repository.RecommendTripRepository;
import moheng.trip.application.TripService;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import moheng.trip.dto.FindTripResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripKeywordCreateRequest;
import moheng.trip.exception.NoExistTripException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;


public class KeywordServiceTest extends ServiceTestConfig {

    @Autowired
    private KeywordService keywordService;

    @Autowired
    private TripService tripService;

    @Autowired
    private RecommendTripRepository recommendTripRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private TripKeywordRepository tripKeywordRepository;

    @Autowired
    private TripRepository tripRepository;

    @DisplayName("키워드를 생성한다.")
    @Test
    void 키워드를_생성한다() {
        // given
        KeywordCreateRequest request = new KeywordCreateRequest("키워드");

        // when, then
        assertDoesNotThrow(() -> keywordService.createKeyword(request));
    }

    @DisplayName("모든 키워드를 찾는다.")
    @Test
    void 모든_키워드를_찾는다() {
        // given
        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));

        // when, then
        assertThat(keywordService.findAllKeywords().getFindAllKeywordResponses()).hasSize(2);
    }

    @DisplayName("키워드 리스트를 찾는다.")
    @Test
    void 키워드_리스트를_찾는다() {
        // given
        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));

        // when, then
        assertThat(keywordService.findNamesByIds(new TripsByKeyWordsRequest(List.of(1L, 2L)))).hasSize(2);
    }

    @DisplayName("키워드로 필터링된 여행지를 추천받는다.")
    @Test
    void 키워드로_필터링된_여행지를_추천받는다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);

        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));
        keywordRepository.save(new Keyword("키워드3"));

        tripService.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1"));
        tripService.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2"));
        tripService.save(new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3"));

        tripKeywordRepository.save(new TripKeyword(tripService.findById(1L), keywordRepository.findById(1L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(2L), keywordRepository.findById(2L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(3L), keywordRepository.findById(2L).get()));

        List<Long> keywordIds = List.of(1L, 2L, 3L);
        TripsByKeyWordsRequest request = new TripsByKeyWordsRequest(keywordIds);

        // when, then
        FindTripsResponse response = keywordService.findRecommendTripsByKeywords(request);
        assertThat(response.getFindTripResponses()).hasSize(3);
    }

    @DisplayName("키워드로 필터링된 여행지는 중복이 제거되어 필터링된다.")
    @Test
    void 키워드로_필터링된_여행지는_중복이_제거되어_필터링된다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);

        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));
        keywordRepository.save(new Keyword("키워드3"));

        tripService.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1"));
        tripService.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2"));
        tripService.save(new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3"));

        tripKeywordRepository.save(new TripKeyword(tripService.findById(1L), keywordRepository.findById(1L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(2L), keywordRepository.findById(2L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(3L), keywordRepository.findById(2L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(3L), keywordRepository.findById(2L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(2L), keywordRepository.findById(2L).get()));

        List<Long> keywordIds = List.of(1L, 2L, 3L);
        TripsByKeyWordsRequest request = new TripsByKeyWordsRequest(keywordIds);

        // when, then
        FindTripsResponse response = keywordService.findRecommendTripsByKeywords(request);
        assertThat(response.getFindTripResponses()).hasSize(3);
    }

    @DisplayName("존재하지 않는 일부 키워드가 존재하는 경우 예외가 발생한다.")
    @Test
    void 존재하지_않는_일부_키워드가_존재하는_경우_예외가_발생한다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);

        List<Long> keywordIds = List.of(-1L, -2L, -3L);
        TripsByKeyWordsRequest request = new TripsByKeyWordsRequest(keywordIds);

        // when, then
        assertThatThrownBy(() -> keywordService.findRecommendTripsByKeywords(request))
                .isInstanceOf(NoExistKeywordException.class);
    }

    @DisplayName("여행지의 키워드를 생성한다.")
    @Test
    void 여행지의_키워드를_생성한다() {
        // given
        keywordRepository.save(new Keyword("키워드1"));
        tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "이미지"));

        TripKeywordCreateRequest request = new TripKeywordCreateRequest(1L, 1L);

        // when
        keywordService.createTripKeyword(request);

        // then
        assertThat(keywordRepository.findById(1L)).isNotEmpty();
    }

    @DisplayName("존재하지 않는 여행지의 키워드를 생성하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지의_키워드를_생성하면_예외가_발생한다() {
        // given
        keywordRepository.save(new Keyword("키워드1"));

        TripKeywordCreateRequest request = new TripKeywordCreateRequest(-1L, 1L);

        // when, then
        assertThatThrownBy(() -> keywordService.createTripKeyword(request))
                .isInstanceOf(NoExistTripException.class);
    }

    @DisplayName("존재하지 않는 키워드로 여행지 키워드를 생성하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_키워드로_여행지_키워드를_생성하면_예외가_발생한다() {
        // given
        tripRepository.save(new Trip("여행지", "장소명", 1L, "설명", "이미지"));

        TripKeywordCreateRequest request = new TripKeywordCreateRequest(1L, -1L);

        // when, then
        assertThatThrownBy(() -> keywordService.createTripKeyword(request))
                .isInstanceOf(NoExistKeywordException.class);
    }

    @DisplayName("랜덤 키워드로 추천 여행지를 찾는다.")
    @Test
    void 랜덤_키워드로_추천_여행지를_찾는다() {
        // given
        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));
        keywordRepository.save(new Keyword("키워드3"));

        tripService.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1"));
        tripService.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2"));
        tripService.save(new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3"));

        tripKeywordRepository.save(new TripKeyword(tripService.findById(1L), keywordRepository.findById(1L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(2L), keywordRepository.findById(2L).get()));
        tripKeywordRepository.save(new TripKeyword(tripService.findById(3L), keywordRepository.findById(3L).get()));

        // when
        FindTripsWithRandomKeywordResponse response = keywordService.findRecommendTripsByRandomKeyword();
        assertThat(response.getFindTripResponses()).isNotEmpty();
    }

    @DisplayName("랜덤 키워드에 관련한 여행지를 상위 조회순으로 찾는다.")
    @ParameterizedTest
    @MethodSource("여행지를_찾는다")
    void 랜덤_키워드에_관련한_여행지를_상위_조회순으로_찾는다(List<Trip> trips, List<String> expectedTripNames) {
        // given
        keywordRepository.save(new Keyword("키워드1"));

        tripRepository.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1", 100L));
        tripRepository.save(new Trip("여행지2", "장소명1", 1L, "설명1", "이미지 경로1", 300L));
        tripRepository.save(new Trip("여행지3", "장소명1", 1L, "설명1", "이미지 경로1", 200L));

        for (final Trip inputTrip : trips) {
            Trip trip = tripRepository.save(inputTrip);
            tripKeywordRepository.save(new TripKeyword(trip, keywordRepository.findById(1L).get()));
        }

        // when
        FindTripsWithRandomKeywordResponse response = keywordService.findRecommendTripsByRandomKeyword();

        // then
        assertAll(() -> {
            assertThat(response.getFindTripResponses()).hasSize(trips.size());
            for (int i = 0; i < expectedTripNames.size(); i++) {
                assertThat(response.getFindTripResponses().get(i).getName()).isEqualTo(expectedTripNames.get(i));
            }
        });
    }

    static Stream<Arguments> 여행지를_찾는다() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1", 100L),
                                new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2", 300L),
                                new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3", 200L)
                        ),
                        List.of("여행지2", "여행지3", "여행지1")
                )
        );
    }


    @DisplayName("랜덤 키워드로 여행지 리스트 조회시 각 여행지에 관련된 모든 키워드를 함께 조회한다.")
    @Test
    void 랜덤_키워드로_여행지_리스트_조회시_각_여행지에_관련된_모든_키워드를_함꼐_조회한다() {
        // given
        Keyword keyword1 = keywordRepository.save(new Keyword("현재 키워드"));
        Keyword keyword2 = keywordRepository.save(new Keyword("다른 키워드2"));
        Keyword keyword3 = keywordRepository.save(new Keyword("다른 키워드3"));

        Trip trip1 = tripRepository.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1", 100L));
        Trip trip2 = tripRepository.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2", 300L));
        Trip trip3 = tripRepository.save(new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3", 200L));

        tripKeywordRepository.save(new TripKeyword(trip1, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip1, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip1, keyword3));
        tripKeywordRepository.save(new TripKeyword(trip2, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip2, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip2, keyword3));
        tripKeywordRepository.save(new TripKeyword(trip3, keyword1));
        tripKeywordRepository.save(new TripKeyword(trip3, keyword2));
        tripKeywordRepository.save(new TripKeyword(trip3, keyword3));

        // when
        FindTripsWithRandomKeywordResponse response = keywordService.findRecommendTripsByRandomKeyword();

        assertAll(() -> {
            assertThat(response.getFindTripResponses().get(0).getKeywords()).hasSize(3);
            assertThat(response.getFindTripResponses().get(1).getKeywords()).hasSize(3);
            assertThat(response.getFindTripResponses().get(2).getKeywords()).hasSize(3);
        });
    }
}
