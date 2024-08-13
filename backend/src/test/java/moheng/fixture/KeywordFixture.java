package moheng.fixture;

import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.keyword.exception.TripRecommendByKeywordRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripResponse;
import moheng.trip.dto.FindTripsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordFixture {
    public static KeywordCreateRequest 키워드_생성_요청() {
        return new KeywordCreateRequest("키워드");
    }

    public static TripsByKeyWordsRequest 키워드_기반_추천_여행지_요청() {
        return new TripsByKeyWordsRequest(List.of(1L, 2L, 3L));
    }

    public static FindTripsResponse 키워드_기반_추천_여행지_응답() {
        final List<Trip> tripList = List.of(
                new Trip("롯데월드1", "서울특별시 송파구", 11820280L, "롯데월드 설명", "https://lotte-world.ong"),
        new Trip("경복궁", "서울특별시 송파구", 82200L, "경복궁 설명", "https://hi-trip.ong"),
        new Trip("이 세상 어딘가 여행지", "서울특별시 송파구", 521928L, "여행지 설명", "https://hi-trip2.ong"));

        return new FindTripsResponse(tripList);
    }
}
