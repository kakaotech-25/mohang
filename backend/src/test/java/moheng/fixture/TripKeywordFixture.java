package moheng.fixture;

import moheng.keyword.domain.Keyword;
import moheng.keyword.domain.TripKeyword;
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.trip.domain.Trip;
import moheng.trip.dto.TripCreateRequest;

import java.time.LocalDate;

public class TripKeywordFixture {
    // 여행지 키워드 생성
    public static TripKeyword 여행지_키워드_생성(Trip trip, Keyword keyword) {
        return new TripKeyword(trip, keyword);
    }
}
