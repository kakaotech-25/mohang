package moheng.planner.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.planner.domain.TripSchedule;
import moheng.planner.domain.TripScheduleRepository;
import moheng.planner.dto.FindPLannerOrderByNameResponse;
import moheng.planner.dto.FindPlannerOrderByDateResponse;
import moheng.planner.dto.FindPlannerOrderByRecentResponse;
import moheng.planner.dto.TripScheduleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class PlannerServiceTest extends ServiceTestConfig {
    @Autowired
    private PlannerService plannerService;

    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("플래너 여행 일정을 최신순인 생성날짜를 기준으로 내림차순 정렬한다.")
    @Test
    void 플래너_여행_일정을_최신순인_생성날짜를_기준으로_내림차순_정렬한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripScheduleRepository.save(new TripSchedule("일정1", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정2", LocalDate.of(2021, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정3", LocalDate.of(2022, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정4", LocalDate.of(2023, 8, 1), LocalDate.of(2024, 8, 2), member));

        // when
        FindPlannerOrderByRecentResponse response = plannerService.findPlannerOrderByRecent(member.getId());
        List<TripScheduleResponse> tripScheduleResponses = response.getTripScheduleResponses();

        // then
        assertAll(() -> {
            assertThat(tripScheduleResponses.get(0).getScheduleName()).isEqualTo("일정4");
            assertThat(tripScheduleResponses.get(3).getScheduleName()).isEqualTo("일정1");
            assertThat(tripScheduleResponses.size()).isEqualTo(4);
        });
    }

    @DisplayName("플래너 여행 일정을 날짜순인 시작날짜를 기준으로 오름차순 정렬한다.")
    @Test
    void 플래너_여행_일정을_날짜순인_시작날짜를_기준으로_오름차순_정렬한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripScheduleRepository.save(new TripSchedule("일정1", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정2", LocalDate.of(2021, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정3", LocalDate.of(2022, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정4", LocalDate.of(2023, 8, 1), LocalDate.of(2024, 8, 2), member));

        // when
        FindPlannerOrderByDateResponse response = plannerService.findPlannerOrderByDateAsc(member.getId());
        List<TripScheduleResponse> tripScheduleResponses = response.getTripScheduleResponses();

        // then
        assertAll(() -> {
            assertThat(tripScheduleResponses.get(0).getScheduleName()).isEqualTo("일정1");
            assertThat(tripScheduleResponses.get(3).getScheduleName()).isEqualTo("일정4");
            assertThat(tripScheduleResponses.size()).isEqualTo(4);
        });
    }

    @DisplayName("플래너 여행 일정을 이름순으로 오름차순 정렬한다.")
    @Test
    void 플래너_여행_일정을_이름순으로_오름차순_정렬한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripScheduleRepository.save(new TripSchedule("가 일정", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("나 일정", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("다 일정", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("라 일정", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));

        // when
        FindPLannerOrderByNameResponse response = plannerService.findPlannerOrderByName(member.getId());
        List<TripScheduleResponse> tripScheduleResponses = response.getTripScheduleResponses();

        // then
        assertAll(() -> {
            assertThat(tripScheduleResponses.get(0).getScheduleName()).isEqualTo("가 일정");
            assertThat(tripScheduleResponses.get(3).getScheduleName()).isEqualTo("라 일정");
            assertThat(tripScheduleResponses.size()).isEqualTo(4);
        });
    }
}
