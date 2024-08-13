package moheng.keyword.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.config.ServiceTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.KeywordRepository;
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

    @DisplayName("키워드와 멤버의 선호 여행지로 여행지를 추천받는다.")
    @Test
    void 키워드와_멤버의_선호_여행지로_여행지를_추천받는다() {
        // given
        memberService.save(하온_기존());
        Member member = memberService.findByEmail(하온_이메일);

        keywordRepository.save(new Keyword("키워드1"));
        keywordRepository.save(new Keyword("키워드2"));

        tripService.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1"));
        tripService.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2"));
        tripService.save(new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3"));

        recommendTripRepository.save(new RecommendTrip(tripService.findById(1L), member, 1L));
        recommendTripRepository.save(new RecommendTrip(tripService.findById(2L), member, 2L));
        recommendTripRepository.save(new RecommendTrip(tripService.findById(3L), member, 3L));

        TripsByKeyWordsRequest request = new TripsByKeyWordsRequest(List.of(1L, 2L, 3L));
        keywordService.findRecommendTripsByKeywords(member.getId(), new TripsByKeyWordsRequest(List.of(1L, 2L, 3L)));

        // when, then
        FindTripsResponse response = keywordService.findRecommendTripsByKeywords(1L, request);
        assertThat(response.getFindTripResponses()).hasSize(3);
    }
}
