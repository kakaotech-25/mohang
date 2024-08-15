package moheng.keyword.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.TripKeywordRepository;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.service.KeywordService;
import moheng.member.application.MemberService;
import moheng.member.domain.Member;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.domain.RecommendTripRepository;
import moheng.trip.application.TripService;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


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

    @DisplayName("키워드를 생성한다.")
    @Test
    void 키워드를_생성한다() {
        // given
        KeywordCreateRequest request = new KeywordCreateRequest("키워드");

        // when, then
        assertDoesNotThrow(() -> keywordService.createKeyword(request));
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
}
