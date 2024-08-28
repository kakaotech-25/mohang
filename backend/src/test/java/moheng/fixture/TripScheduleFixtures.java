package moheng.fixture;

import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.planner.dto.UpdateTripScheduleRequest;

import java.time.LocalDate;

public class TripScheduleFixtures {
    public static CreateTripScheduleRequest 여행_일정_생성_요청() {
        return new CreateTripScheduleRequest("제주도 여행", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }

    public static CreateTripScheduleRequest 유효하지_않은_이름으로_여행_일정_생성_요청() {
        return new CreateTripScheduleRequest("a", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }

    public static CreateTripScheduleRequest 유효하지_않은_날짜로_여행_일정_생성_요청() {
        return new CreateTripScheduleRequest("a", LocalDate.of(2020, 1, 1), LocalDate.of(1999, 1, 10));
    }

    public static UpdateTripScheduleRequest 여행_일정_수정_요청() {
        return new UpdateTripScheduleRequest(1L, "수정된 제주도 여행", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10));
    }
}
