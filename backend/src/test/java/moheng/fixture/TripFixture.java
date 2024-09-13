package moheng.fixture;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.keyword.dto.FindTripsWithRandomKeywordResponse;
import moheng.keyword.dto.TripsByKeyWordsRequest;
import moheng.member.domain.GenderType;
import moheng.member.dto.request.SignUpProfileRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TripFixture {
    // 여행지 생성
    public static Trip 여행지1_생성() {
        return new Trip("여행지1", "장소명1", 1L, "여행지 설명1",
                "https://trip1.png", 123.123, 123.123);
    }

    public static Trip 여행지2_생성() {
        return new Trip("여행지2", "장소명2", 2L, "여행지 설명2",
                "https://trip1.png", 123.123, 123.123);
    }

    public static Trip 여행지3_생성() {
        return new Trip("여행지3", "장소명3", 3L, "여행지 설명3",
                "https://trip3.png", 123.123, 123.123);
    }

    public static Trip 여행지4_생성() {
        return new Trip("여행지4", "장소명4", 4L, "여행지 설명4",
                "https://trip4.png", 123.123, 123.123);
    }

    public static Trip 여행지5_생성() {
        return new Trip("여행지5", "장소명5", 5L, "여행지 설명5",
                "https://trip5.png", 123.123, 123.123);
    }

    public static Trip 여행지6_생성() {
        return new Trip("여행지6", "장소명6", 6L, "여행지 설명6",
                "https://trip6.png", 123.123, 123.123);
    }

    public static Trip 여행지7_생성() {
        return new Trip("여행지7", "장소명7", 7L, "여행지 설명7",
                "https://trip7.png", 123.123, 123.123);
    }

    public static Trip 여행지8_생성() {
        return new Trip("여행지8", "장소명8", 8L, "여행지 설명8",
                "https://trip8.png", 123.123, 123.123);
    }

    public static Trip 여행지9_생성() {
        return new Trip("여행지9", "장소명9", 9L, "여행지 설명9",
                "https://trip9.png", 123.123, 123.123);
    }

    public static Trip 여행지10_생성() {
        return new Trip("여행지10", "장소명10", 10L, "여행지 설명10",
                "https://trip10.png", 123.123, 123.123);
    }

    public static Trip 여행지11_생성() {
        return new Trip("여행지11", "장소명11", 11L, "여행지 설명11",
                "https://trip11.png", 123.123, 123.123);
    }

    public static Trip 여행지12_생성() {
        return new Trip("여행지12", "장소명12", 12L, "여행지 설명12",
                "https://trip12.png", 123.123, 123.123);
    }

    // 방문수 고려 여행지 생성
    public static Trip 여행지1_생성_방문수_1등() {
        return new Trip("여행지1", "장소명1", 1L, "여행지 설명1",
                "https://trip1.png", 400L);
    }

    public static Trip 여행지2_생성_방문수_3등() {
        return new Trip("여행지2", "장소명2", 2L, "여행지 설명2",
                "https://trip1.png", 200L);
    }

    public static Trip 여행지3_생성_방문수_2등() {
        return new Trip("여행지3", "장소명3", 1L, "여행지 설명3",
                "https://trip1.png", 300L);
    }

    public static Trip 여행지4_생성_방문수_4등() {
        return new Trip("여행지4", "장소명4", 1L, "여행지 설명3",
                "https://trip1.png", 100L);
    }

    // 키워드로 필터링한 여행지 요청
    public static TripsByKeyWordsRequest 키워드로_필터링한_여행지_리스트_요청(final List<Long> keywordIds) {
        return new TripsByKeyWordsRequest(keywordIds);
    }

    // 여행지 생성 요청
    public static TripCreateRequest 롯데월드_여행지_생성_요청() {
        return new TripCreateRequest("롯데월드", "서울특별시 송파구", 1L, "롯데월드 관련 설명", "https://lotte-world.png");
    }

    // 여행지 키워드 생성 요청
    public static TripKeywordCreateRequest 여행지_키워드_생성_요청(final long tripId, final long keywordId) {
        return new TripKeywordCreateRequest(tripId, keywordId);
    }

    public static TripKeywordCreateRequest 유효하지_않은_여행지_키워드_생성_요청(final long tripId, final long keywordId) {
        return new TripKeywordCreateRequest(tripId, keywordId);
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
