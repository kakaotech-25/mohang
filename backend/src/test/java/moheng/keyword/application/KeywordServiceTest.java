package moheng.keyword.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.config.ServiceTestConfig;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.keyword.service.KeywordService;
import moheng.trip.dto.FindTripResponse;
import moheng.trip.dto.FindTripsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordServiceTest extends ServiceTestConfig {
    @Autowired
    private KeywordService keywordService;

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
        preferredLocations.put(99999999L, 100L);
        preferredLocations.put(99999998L, 200L);
        preferredLocations.put(99999997L, 300L);
        TripRecommendByKeywordRequest request = new TripRecommendByKeywordRequest(keywords, preferredLocations);

        // when, then
        FindTripsResponse response = keywordService.findRecommendTripsByKeywords(request);
        assertThat(response.getFindTripResponses()).isNotEmpty();
    }
}
