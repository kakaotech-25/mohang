package moheng.fixture;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.member.domain.Member;
import moheng.recommendtrip.domain.RecommendTrip;
import moheng.recommendtrip.dto.RecommendTripCreateRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.response.FindTripsResponse;

import java.util.List;

public class RecommendTripFixture {
    // 선호 여행지 생성 요청
    public static RecommendTrip 선호_여행지_생성_랭크1(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 1L);
    }

    public static RecommendTrip 선호_여행지_생성_랭크2(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 2L);
    }

    public static RecommendTrip 선호_여행지_생성_랭크3(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 3L);
    }

    public static RecommendTrip 선호_여행지_생성_랭크4(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 4L);
    }

    public static RecommendTrip 선호_여행지_생성_랭크5(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 5L);
    }

    public static RecommendTrip 선호_여행지_생성_랭크6(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 6L);
    }

    public static RecommendTrip 선호_여행지_생성_랭크7(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 7L);
    }

    public static RecommendTrip 선호_여행지_생성_랭크8(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 8L);
    }

    public static RecommendTrip 선호_여행지_생성_랭크9(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 9L);
    }

    public static RecommendTrip 선호_여행지_생성_랭크10(Trip trip, Member member) {
        return new RecommendTrip(trip, member, 10L);
    }

    // 선호 여행지 생성 요청
    public static RecommendTripCreateRequest 선호_여행지_생성_요청(long tripId) {
        return new RecommendTripCreateRequest(tripId);
    }

    // 존재하지 않는 여행지로 선호 여행지 생성 요청
    public static RecommendTripCreateRequest 유효하지_않은_선호_여행지_생성_요청() {
        return new RecommendTripCreateRequest(-1L);
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
