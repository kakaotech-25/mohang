package moheng.fixture;

import moheng.planner.domain.TripSchedule;
import moheng.planner.dto.FindPLannerOrderByNameResponse;
import moheng.planner.dto.FindPlannerOrderByDateResponse;
import moheng.planner.dto.FindPlannerOrderByRecentResponse;

import java.time.LocalDate;
import java.util.List;

import static moheng.fixture.MemberFixtures.하온_기존;

public class PlannerFixture {

    public static FindPlannerOrderByRecentResponse 플래너_최신순_조회_응답() {
        return new FindPlannerOrderByRecentResponse(List.of(
                new TripSchedule("제주도 여행", LocalDate.of(2020, 3, 10), LocalDate.of(2030, 1, 10), 하온_기존()),
                new TripSchedule("카카오 여행", LocalDate.of(2024, 2, 15), LocalDate.of(2030, 1, 10), 하온_기존()),
                new TripSchedule("네이버 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), 하온_기존()),
                new TripSchedule("우주 여행", LocalDate.of(2023, 1, 1), LocalDate.of(2030, 1, 10), 하온_기존())
        ));
    }

    public static FindPLannerOrderByNameResponse 플래너_이름순_조회_응답() {
        return new FindPLannerOrderByNameResponse(List.of(
                new TripSchedule("가 여행", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), 하온_기존()),
                new TripSchedule("나 여행", LocalDate.of(2023, 2, 15), LocalDate.of(2030, 1, 10), 하온_기존()),
                new TripSchedule("다 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), 하온_기존()),
                new TripSchedule("라 여행", LocalDate.of(2021, 1, 1), LocalDate.of(2030, 1, 10), 하온_기존())
        ));
    }

    public static FindPlannerOrderByDateResponse 플래너_날짜순_조회_응답() {
        return new FindPlannerOrderByDateResponse(List.of(
                new TripSchedule("제주도 여행", LocalDate.of(2024, 3, 10), LocalDate.of(2030, 1, 10), 하온_기존()),
                new TripSchedule("카카오 여행", LocalDate.of(2023, 2, 15), LocalDate.of(2030, 1, 10), 하온_기존()),
                new TripSchedule("네이버 여행", LocalDate.of(2022, 1, 8), LocalDate.of(2030, 1, 10), 하온_기존()),
                new TripSchedule("우주 여행", LocalDate.of(2021, 1, 1), LocalDate.of(2030, 1, 10), 하온_기존())
        ));
    }
}
