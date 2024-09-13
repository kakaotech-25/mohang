package moheng.config.fixture;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.member.domain.GenderType;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.FindTripWithSimilarTripsResponse;
import moheng.trip.dto.FindTripsResponse;
import moheng.trip.dto.SimilarTripResponses;
import moheng.trip.dto.TripCreateRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TripFixture {
    // 여행지 생성 요청
    public static TripCreateRequest 롯데월드_여행지_생성_요청() {
        return new TripCreateRequest("롯데월드", "서울특별시 송파구", 1L, "롯데월드 관련 설명", "https://lotte-world.png");
    }

    public static FindTripsResponse 여행지_응답() {
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

    public static FindTripWithSimilarTripsResponse 여행지_조회_응답() {
        Trip trip = new Trip("여행지1", "장소명1", 1L, "설명1", "이미지 경로1");
        List<String> keywords = Arrays.asList("자연", "모험");
        Trip similarTrip1 = new Trip("여행지2", "장소명2", 2L, "설명2", "이미지 경로2");
        Trip similarTrip2 = new Trip("여행지3", "장소명3", 3L, "설명3", "이미지 경로3");

        TripKeyword tripKeyword1 = new TripKeyword(similarTrip1, new Keyword("역사"));
        TripKeyword tripKeyword2 = new TripKeyword(similarTrip2, new Keyword("문화"));

        SimilarTripResponses similarTripResponses = new SimilarTripResponses(
                Arrays.asList(similarTrip1, similarTrip2),
                Arrays.asList(tripKeyword1, tripKeyword2)
        );

        return new FindTripWithSimilarTripsResponse(trip, keywords, similarTripResponses);
    }

}
