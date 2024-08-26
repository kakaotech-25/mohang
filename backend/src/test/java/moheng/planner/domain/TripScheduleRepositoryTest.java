package moheng.planner.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moheng.config.slice.RepositoryTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.planner.dto.FindPlannerOrderByRecentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class TripScheduleRepositoryTest extends RepositoryTestConfig {
    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("여행 일정을 생성날짜 기준으로 내림차순 정렬한다.")
    @Test
    void 여행_일정을_생성날짜_기준으로_내림차순_정렬한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripScheduleRepository.save(new TripSchedule("일정1", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정3", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정2", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));

        // when
        List<TripSchedule> actual = tripScheduleRepository.findByMemberOrderByCreatedAtDesc(member);

        // then
        assertAll(() -> {
            assertThat(actual.get(0).getName()).isEqualTo("일정2");
            assertThat(actual.get(1).getName()).isEqualTo("일정3");
            assertThat(actual.get(2).getName()).isEqualTo("일정1");
        });
    }

    @DisplayName("여행 일정을 시작날짜 기준으로 오름차순 정렬한다.")
    @Test
    void 여행_일정을_시작날짜_기준으로_오름차순_정렬한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripScheduleRepository.save(new TripSchedule("일정1", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정2", LocalDate.of(2022, 8, 3), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("일정3", LocalDate.of(2021, 8, 2), LocalDate.of(2024, 8, 2), member));

        // when
        List<TripSchedule> actual = tripScheduleRepository.findByMemberOrderByStartDateAsc(member);

        // then
        assertAll(() -> {
            assertThat(actual.get(0).getName()).isEqualTo("일정1");
            assertThat(actual.get(1).getName()).isEqualTo("일정3");
            assertThat(actual.get(2).getName()).isEqualTo("일정2");
        });
    }

    @DisplayName("여행 일정을 이름순 기준으로 오름차순 정렬한다.")
    @Test
    void 여행_일정을_이름순_기준으로_오름차순_정렬한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripScheduleRepository.save(new TripSchedule("가 일정", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("다 일정", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));
        tripScheduleRepository.save(new TripSchedule("나 일정", LocalDate.of(2020, 8, 1), LocalDate.of(2024, 8, 2), member));

        // when
        List<TripSchedule> actual = tripScheduleRepository.findByMemberOrderByNameAsc(member);

        // then
        assertAll(() -> {
            assertThat(actual.get(0).getName()).isEqualTo("가 일정");
            assertThat(actual.get(1).getName()).isEqualTo("나 일정");
            assertThat(actual.get(2).getName()).isEqualTo("다 일정");
        });
    }
}
