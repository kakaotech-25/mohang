package moheng.fixture;

import moheng.keyword.exception.TripRecommendByKeywordRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordFixture {
    public static TripRecommendByKeywordRequest 키워드_기반_추천_여행지_요청() {
        List<String> 키워드_리스트 = List.of("키워드1", "키워드2", "키워드3");
        Map<Long, Long> 선호_여행지 = new HashMap<>();
        선호_여행지.put(1L, 100L);
        선호_여행지.put(2L, 100L);
        선호_여행지.put(3L, 100L);
        return new TripRecommendByKeywordRequest(키워드_리스트, 선호_여행지);
    }
}
