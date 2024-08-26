package moheng.planner.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.planner.dto.CreateTripScheduleRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class TripScheduleServiceTest extends ServiceTestConfig {
    @Autowired
    private TripScheduleService tripScheduleService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("여행 일정을 생성한다.")
    @Test
    void 여행_일정을_생성한다() {
        // given
        Member member = memberRepository.save(하온_기존());

        // when, then
        assertDoesNotThrow(() -> {
            tripScheduleService.createTripSchedule(
                    member.getId(),
                    new CreateTripScheduleRequest("일정1", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2))
            );
        });
    }
}
