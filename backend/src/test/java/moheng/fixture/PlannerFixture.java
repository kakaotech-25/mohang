package moheng.fixture;

import moheng.planner.domain.TripSchedule;
import moheng.planner.dto.request.FindPlannerOrderByDateBetweenRequest;
import moheng.planner.dto.request.FindPublicSchedulesForRangeRequest;
import moheng.planner.dto.request.FindSchedulesByNameRequest;
import moheng.planner.dto.request.FindSchedulesNameResponses;
import moheng.planner.dto.response.FindPLannerOrderByNameResponse;
import moheng.planner.dto.response.FindPlannerOrderByDateBetweenResponse;
import moheng.planner.dto.response.FindPlannerOrderByDateResponse;
import moheng.planner.dto.response.FindPlannerOrderByRecentResponse;

import java.time.LocalDate;
import java.util.List;

import static moheng.fixture.MemberFixtures.하온_기존;

public class PlannerFixture {
    // 플래너 요청
    public static FindPlannerOrderByDateBetweenRequest 플래너_날짜순_범위_조회_요청() {
        return new FindPlannerOrderByDateBetweenRequest(
                LocalDate.of(2024, 1, 14),
                LocalDate.of(2025, 6, 15)
        );
    }

    public static FindPublicSchedulesForRangeRequest 플래너_생성날짜_기준_범위_내의_공개된_여행지_조회_요청() {
        return new FindPublicSchedulesForRangeRequest(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 3, 10));
    }

    public static FindPublicSchedulesForRangeRequest 잘못된_시작날짜_종료날짜로_플래너_생성날짜_기준_범위_내의_공개된_여행지_조회_요청() {
        return new FindPublicSchedulesForRangeRequest(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 1, 10));
    }

    public static FindSchedulesByNameRequest 여행_일정_검색명_조회_요청() {
        return new FindSchedulesByNameRequest("제주도");
    }

    // 플래너 응답
    public static FindPlannerOrderByRecentResponse 플래너_최신순_조회_응답() {
        return new FindPlannerOrderByRecentResponse(List.of(
                new TripSchedule("제주도 여행", LocalDate.of(2020, 3, 10), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("카카오 여행", LocalDate.of(2024, 2, 15), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("네이버 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("우주 여행", LocalDate.of(2023, 1, 1), LocalDate.of(2030, 1, 10), false, 하온_기존())
        ));
    }

    public static FindPLannerOrderByNameResponse 플래너_이름순_조회_응답() {
        return new FindPLannerOrderByNameResponse(List.of(
                new TripSchedule("가 여행", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("나 여행", LocalDate.of(2023, 2, 15), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("다 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("라 여행", LocalDate.of(2021, 1, 1), LocalDate.of(2030, 1, 10), false, 하온_기존())
        ));
    }

    public static FindPlannerOrderByDateResponse 플래너_날짜순_조회_응답() {
        return new FindPlannerOrderByDateResponse(List.of(
                new TripSchedule("제주도 여행", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("카카오 여행", LocalDate.of(2023, 2, 15), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("네이버 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("우주 여행", LocalDate.of(2021, 1, 1), LocalDate.of(2030, 1, 10), false, 하온_기존())
        ));
    }

    public static FindPlannerOrderByDateBetweenResponse 플래너_날짜순_범위_조회_응답() {
        return new FindPlannerOrderByDateBetweenResponse(List.of(
                new TripSchedule("제주도 여행", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("카카오 여행", LocalDate.of(2023, 2, 15), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("네이버 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("우주 여행", LocalDate.of(2021, 1, 1), LocalDate.of(2030, 1, 10), false, 하온_기존())
        ));
    }

    public static FindSchedulesNameResponses 여행_일정_검색명_조회_응답() {
        return new FindSchedulesNameResponses(List.of(
                new TripSchedule("제주도 여행 일정", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), false, 하온_기존()),
                new TripSchedule("제주도 가자!", LocalDate.of(2023, 2, 15), LocalDate.of(2030, 1, 10), false, 하온_기존())
        ));
    }
}
