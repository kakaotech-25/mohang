package moheng.planner.application;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moheng.config.slice.ServiceTestConfig;
import moheng.member.domain.Member;
import moheng.member.domain.repository.MemberRepository;
import moheng.member.exception.NoExistMemberException;
import moheng.planner.dto.CreateTripScheduleRequest;
import moheng.planner.exception.AlreadyExistTripScheduleException;
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

    @DisplayName("동일한 이름을 가진 멤버의 여행 일정이 이미 존재하면 예외가 발생한다.")
    @Test
    void 동일한_이름을_가진_멤버의_여행_일정이_이미_존재하면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(하온_기존());
        tripScheduleService.createTripSchedule(member.getId(),
                new CreateTripScheduleRequest("기존일정", LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2)));

        // when, then
        assertThatThrownBy(() ->
            tripScheduleService.createTripSchedule(member.getId(),
                    new CreateTripScheduleRequest("기존일정", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 2))
        )).isInstanceOf(AlreadyExistTripScheduleException.class);
    }

    @DisplayName("존재하지 않는 멤버의 여행 일정을 생성하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_멤버의_여행_일정을_생성하면_예외가_발생한다() {
        // given
        long invalidMemberId = -1L;

        // when, then
        assertThatThrownBy(() ->
                tripScheduleService.createTripSchedule(invalidMemberId,
                        new CreateTripScheduleRequest("신규일정", LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 2))
                )).isInstanceOf(NoExistMemberException.class);
    }
}
