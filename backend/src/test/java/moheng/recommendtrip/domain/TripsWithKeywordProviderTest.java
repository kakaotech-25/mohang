package moheng.recommendtrip.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.domain.repository.KeywordRepository;
import moheng.keyword.domain.repository.TripKeywordRepository;
import moheng.recommendtrip.domain.tripfilterstrategy.TripsWithKeywordProvider;
import moheng.recommendtrip.exception.LackOfRecommendTripException;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static moheng.fixture.KeywordFixture.*;
import static moheng.fixture.KeywordFixture.키워드10_생성;
import static moheng.fixture.TripFixture.*;
import static moheng.fixture.TripFixture.여행지10_생성;

public class TripsWithKeywordProviderTest extends ServiceTestConfig {
    @Autowired
    private TripsWithKeywordProvider tripsWithKeywordProvider;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private TripKeywordRepository tripKeywordRepository;

    @DisplayName("여행지 10개를 키워드와 함께 반환한다.")
    @Test
    void 여행지를_키워드와_함께_반환한다() {
        // given
        List<Trip> 여행지_리스트 = new ArrayList<>();
        Trip 여행지1 = tripRepository.save(여행지1_생성()); Trip 여행지2 = tripRepository.save(여행지2_생성());
        Trip 여행지3 = tripRepository.save(여행지3_생성()); Trip 여행지4 = tripRepository.save(여행지4_생성());
        Trip 여행지5 = tripRepository.save(여행지5_생성()); Trip 여행지6 = tripRepository.save(여행지6_생성());
        Trip 여행지7 = tripRepository.save(여행지7_생성()); Trip 여행지8 = tripRepository.save(여행지8_생성());
        Trip 여행지9 = tripRepository.save(여행지9_생성()); Trip 여행지10 = tripRepository.save(여행지10_생성());

        여행지_리스트.add(여행지1); 여행지_리스트.add(여행지2); 여행지_리스트.add(여행지3); 여행지_리스트.add(여행지4); 여행지_리스트.add(여행지5);
        여행지_리스트.add(여행지6); 여행지_리스트.add(여행지7); 여행지_리스트.add(여행지8); 여행지_리스트.add(여행지9); 여행지_리스트.add(여행지10);

        Keyword 키워드1 = keywordRepository.save(키워드1_생성()); Keyword 키워드2 = keywordRepository.save(키워드2_생성());
        Keyword 키워드3 = keywordRepository.save(키워드3_생성()); Keyword 키워드4 = keywordRepository.save(키워드4_생성());
        Keyword 키워드5 = keywordRepository.save(키워드5_생성()); Keyword 키워드6 = keywordRepository.save(키워드6_생성());
        Keyword 키워드7 = keywordRepository.save(키워드7_생성()); Keyword 키워드8 = keywordRepository.save(키워드8_생성());
        Keyword 키워드9 = keywordRepository.save(키워드9_생성()); Keyword 키워드10 = keywordRepository.save(키워드10_생성());

        tripKeywordRepository.save(new TripKeyword(여행지1, 키워드1)); tripKeywordRepository.save(new TripKeyword(여행지2, 키워드2));
        tripKeywordRepository.save(new TripKeyword(여행지3, 키워드3)); tripKeywordRepository.save(new TripKeyword(여행지4, 키워드4));
        tripKeywordRepository.save(new TripKeyword(여행지5, 키워드5)); tripKeywordRepository.save(new TripKeyword(여행지6, 키워드6));
        tripKeywordRepository.save(new TripKeyword(여행지7, 키워드7)); tripKeywordRepository.save(new TripKeyword(여행지8, 키워드8));
        tripKeywordRepository.save(new TripKeyword(여행지9, 키워드9)); tripKeywordRepository.save(new TripKeyword(여행지10, 키워드10));


        // when, then
        assertEquals(tripsWithKeywordProvider.findWithKeywords(여행지_리스트).getFindTripResponses().size(), 10);
    }

    @DisplayName("전달받은 추천 여행지가 정확히 10개가 아니라면 예외가 발생한다.")
    @Test
    void 전달받은_추천_여행지가_정확히_10개가_아니라면_예외가_발생한다() {
        // given
        List<Trip> 여행지_리스트_크기_0 = new ArrayList<>();

        // when, then
        assertThatThrownBy(() -> tripsWithKeywordProvider.findWithKeywords(여행지_리스트_크기_0))
                .isInstanceOf(LackOfRecommendTripException.class);
    }
}
