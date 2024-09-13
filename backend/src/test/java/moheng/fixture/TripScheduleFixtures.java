package moheng.fixture;

import moheng.member.domain.Member;
import moheng.planner.domain.TripSchedule;
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.planner.dto.UpdateTripScheduleRequest;

import java.time.LocalDate;

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

    // 여행 일정 생성 요청
    public static CreateTripScheduleRequest 여행_일정_생성_요청() {
        return new CreateTripScheduleRequest("제주도 여행", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
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
}
