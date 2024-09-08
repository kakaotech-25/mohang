package moheng.fixture;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.dto.FindTripsWithRandomKeywordResponse;
import moheng.keyword.dto.KeywordCreateRequest;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.TripKeywordCreateRequest;

import java.util.List;

public class KeywordFixture {
    public static KeywordCreateRequest 키워드_생성_요청() {
        return new KeywordCreateRequest("키워드");
    }

    public static TripKeywordCreateRequest 여행지_키워드_생성_요청() {
        return new TripKeywordCreateRequest(1L, 1L);
    }

    public static TripsByKeyWordsRequest 키워드_기반_추천_여행지_요청() {
        return new TripsByKeyWordsRequest(List.of(1L, 2L, 3L));
    }

    public static FindTripsResponse 키워드_기반_추천_여행지_응답() {
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

    public static FindTripsWithRandomKeywordResponse 랜덤_키워드_기반_추천_여행지_응답() {
        TripKeyword tripKeyword1 = new TripKeyword
                (new Trip("롯데월드1", "서울특별시 송파구1", 20280L, "롯데월드 설명1",
                        "https://lotte-world.ong"), new Keyword("키워드1"));

        TripKeyword tripKeyword2 = new TripKeyword
                (new Trip("롯데월드2", "서울특별시 송파구2", 1182080L, "롯데월드 설명2",
                        "https://lotte-world.ong"), new Keyword("키워드1"));

        TripKeyword tripKeyword3 = new TripKeyword
                (new Trip("롯데월드3", "서울특별시 송파구3", 9820280L, "롯데월드 설명3",
                        "https://lotte-world.ong"), new Keyword("키워드1"));

        return new FindTripsWithRandomKeywordResponse(List.of(tripKeyword1, tripKeyword2, tripKeyword3), new Keyword("키워드1"));
    }
}
