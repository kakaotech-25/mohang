package moheng.fixture;

import moheng.member.domain.Member;
import moheng.planner.domain.TripSchedule;
import moheng.planner.domain.TripScheduleRegistry;
import moheng.planner.dto.AddTripOnScheduleRequests;
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.planner.dto.UpdateTripOrdersRequest;
import moheng.planner.dto.UpdateTripScheduleRequest;
import moheng.trip.domain.Trip;

import java.time.LocalDate;
import java.util.List;

public class TripScheduleFixtures {
    // 여행 일정 생성
    public static TripSchedule 여행_일정1_생성(Member member) {
        return new TripSchedule("일정1", LocalDate.of(2000, 1, 1), LocalDate.of(2020, 1, 10), member);
    }

    public static TripSchedule 여행_일정2_생성(Member member) {
        return new TripSchedule("일정2", LocalDate.of(2001, 1, 1), LocalDate.of(2020, 1, 10), member);
    }

    public static TripSchedule 여행_일정3_생성(Member member) {
        return new TripSchedule("일정3", LocalDate.of(2002, 1, 1), LocalDate.of(2020, 1, 10), member);
    }

    public static TripSchedule 여행_일정4_생성(Member member) {
        return new TripSchedule("일정4", LocalDate.of(2003, 1, 1), LocalDate.of(2020, 1, 10), member);
    }

    public static TripSchedule 여행_일정_가_생성(Member member) {
        return new TripSchedule("가 일정", LocalDate.of(2000, 1, 1), LocalDate.of(2020, 1, 10), member);
    }

    public static TripSchedule 여행_일정_나_생성(Member member) {
        return new TripSchedule("나 일정", LocalDate.of(2001, 1, 1), LocalDate.of(2020, 1, 10), member);
    }

    public static TripSchedule 여행_일정_다_생성(Member member) {
        return new TripSchedule("다 일정", LocalDate.of(2002, 1, 1), LocalDate.of(2020, 1, 10), member);
    }

    public static TripSchedule 여행_일정_라_생성(Member member) {
        return new TripSchedule("라 일정", LocalDate.of(2003, 1, 1), LocalDate.of(2020, 1, 10), member);
    }

    public static TripSchedule 여행_일정_중복_생성(Member member) {
        return new TripSchedule("중복 일정", LocalDate.of(2003, 1, 1), LocalDate.of(2020, 1, 10), member);
    }

    // 여행지 일정 등록 생성
    public static TripScheduleRegistry 여행_일정_등록_생성(Trip trip, TripSchedule tripSchedule) {
        return new TripScheduleRegistry(trip, tripSchedule);
    }

    // 여행 일정 생성 요청
    public static CreateTripScheduleRequest 제주도_여행_일정_생성_요청() {
        return new CreateTripScheduleRequest("제주도 여행", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }

    public static CreateTripScheduleRequest 여행_일정1_생성_요청() {
        return new CreateTripScheduleRequest("일정1", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }

    public static CreateTripScheduleRequest 중복_여행_일정_생성_요청() {
        return new CreateTripScheduleRequest("중복 일정", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }

    public static CreateTripScheduleRequest 신규_여행_일정_생성_요청() {
        return new CreateTripScheduleRequest("신규 일정", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }

    public static CreateTripScheduleRequest 유효하지_않은_이름으로_여행_일정_생성_요청() {
        return new CreateTripScheduleRequest("a", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }

    public static CreateTripScheduleRequest 유효하지_않은_날짜로_여행_일정_생성_요청() {
        return new CreateTripScheduleRequest("a", LocalDate.of(2020, 1, 1), LocalDate.of(1999, 1, 10));
    }

    // 여행 일정 업데이트 요청
    public static UpdateTripScheduleRequest 여행_일정_수정_요청() {
        return new UpdateTripScheduleRequest(1L, "수정된 제주도 여행", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }

    public static UpdateTripScheduleRequest 중복_여행_일정_수정_요청() {
        return new UpdateTripScheduleRequest(1L, "중복 일정", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }

    // 플래너에 여행지 추가 요청
    public static AddTripOnScheduleRequests 플래너에_여행지_담기_요청(List<Long> tripScheduleIds) {
        return new AddTripOnScheduleRequests(tripScheduleIds);
    }

    // 여행지 정렬 순서 업데이트 요청
    public static UpdateTripOrdersRequest 여행지_정렬_순서_업데이트_요청(List<Long> tripScheduleIds) {
        return new UpdateTripOrdersRequest(tripScheduleIds);
    }

    public static UpdateTripOrdersRequest 유효하지_않은_여행지_정렬_순서_업데이트_요청() {
        return new UpdateTripOrdersRequest(List.of(-1L, -2L, -3L));
    }
}
