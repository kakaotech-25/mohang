package moheng.keyword.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.config.ServiceTestConfig;
import moheng.config.TestConfig;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.keyword.service.KeywordService;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KeywordServiceTest extends ServiceTestConfig {

    @Autowired
    private KeywordService keywordService;

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

    @DisplayName("키워드와 멤버의 선호 여행지로 여행지를 추천받는다.")
    @Test
    void 키워드와_멤버의_선호_여행지로_여행지를_추천받는다() {
        // given
        List<String> keywords = new ArrayList<>();
        Map<Long, Long> preferredLocations = new HashMap<>();
        preferredLocations.put(1L, 100L);
        preferredLocations.put(2L, 200L);
        preferredLocations.put(3L, 300L);
        TripRecommendByKeywordRequest request = new TripRecommendByKeywordRequest(keywords, preferredLocations);

        tripRepository.save(new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1"));
        tripRepository.save(new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2"));
        tripRepository.save(new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3"));

        // when, then
        FindTripsResponse response = keywordService.findRecommendTripsByKeywords(request);
        assertThat(response.getFindTripResponses()).hasSize(3);
    }
}
