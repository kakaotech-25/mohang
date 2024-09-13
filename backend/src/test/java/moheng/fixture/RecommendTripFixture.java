package moheng.fixture;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripCreateRequest;

import java.util.List;

public class RecommendTripFixture {
    // 선호 여행지 생성 요청
    public static RecommendTripCreateRequest 선호_여행지_생성_요청() {
        return new RecommendTripCreateRequest(1L);
    }

    public static FindTripsResponse AI_맞춤_추천_여행지_응답() {
        TripKeyword tripKeyword1 = new TripKeyword
                (new Trip("롯데월드1", "서울특별시 송파구1", 20280L, "롯데월드 설명1",
                        "https://lotte-world.ong"), new Keyword("키워드1"));

        TripKeyword tripKeyword2 = new TripKeyword
                (new Trip("롯데월드2", "서울특별시 송파구2", 1182080L, "롯데월드 설명2",
                        "https://lotte-world.ong"), new Keyword("키워드2"));

        TripKeyword tripKeyword3 = new TripKeyword
                (new Trip("롯데월드3", "서울특별시 송파구3", 9820280L, "롯데월드 설명3",
                        "https://lotte-world.ong"), new Keyword("키워드3"));

        return new FindTripsResponse(List.of(tripKeyword1, tripKeyword2, tripKeyword3));
    }
}
