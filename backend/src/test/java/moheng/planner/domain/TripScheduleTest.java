package moheng.planner.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moheng.planner.exception.InvalidTripScheduleDateException;
import moheng.planner.exception.InvalidTripScheduleNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

public class TripScheduleTest {

    @DisplayName("플래너 일정을 생성한다.")
    @Test
    void 플래너_일정을_생성한다() {
        assertDoesNotThrow(() -> new TripSchedule("일정1",
                LocalDate.of(2024, 8, 26),
                LocalDate.of(2024, 8, 27),
                하온_기존()));
    }

    @DisplayName("플래너 일정의 이름이 유효하지 않다면 예외가 발생한다.")
    @ValueSource(strings = {"", "1", "플", "랜",
            "일이삼사오육칠팔구십 일이삼사오육칠팔구십 일이삼사오육칠팔구십 일이삼사오육칠팔구십 일이삼사오육칠팔구십 일이삼사오육칠팔구십 일이삼사오육칠팔구십 일이삼사오육칠팔구십 일이삼사오육칠팔구십 일이삼사오육칠팔구십 "})
    @ParameterizedTest
    void 플래너_일정의_이름이_유효하지_않다면_예외가_발생한다(final String planName) {
        assertThatThrownBy(() ->
                new TripSchedule(planName, LocalDate.of(2024, 8, 26), LocalDate.of(2024, 8, 27), 하온_기존())
        ).isInstanceOf(InvalidTripScheduleNameException.class);
    }

    @DisplayName("플래너 일정의 시작날짜가 종료날짜의 이후라면 예외가 발생한다.")
    @Test
    void 플래너_일정의_시작날짜가_종료날짜의_이후라면_예외가_발생한다() {
        assertThatThrownBy(() ->
            new TripSchedule("일정", LocalDate.of(2024, 8, 26), LocalDate.of(1000, 1, 1), 하온_기존())
        ).isInstanceOf(InvalidTripScheduleDateException.class);
    }
}
