package moheng.fixture;

import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.dto.TripCreateRequest;

public class RecommendTripFixture {
    // 선호 여행지 생성 요청
    public static RecommendTripCreateRequest 선호_여행지_생성_요청() {
        return new RecommendTripCreateRequest(1L);
    }
}
