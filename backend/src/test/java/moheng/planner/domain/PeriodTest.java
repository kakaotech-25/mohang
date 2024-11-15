package moheng.planner.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.planner.exception.InvalidDateSequenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PeriodTest {

    @DisplayName("일정 기간을 생성한다.")
    @Test
    void 일정_기간을_생성한다() {
        // given
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 12, 31);

        // when, then
        assertDoesNotThrow(() -> new Period(startDate, endDate));
    }

    @DisplayName("시작날짜가 종료날짜 이후라면 예외가 발생한다.")
    @Test
    void 시작날짜가_종료날짜_이후라면_예외가_발생한다() {
        // given
        LocalDate startDate = LocalDate.of(2100, 12, 31);
        LocalDate endDate = LocalDate.of(1800, 1, 1);

        // when, then
        assertThatThrownBy(() -> new Period(startDate, endDate))
                .isInstanceOf(InvalidDateSequenceException.class);
    }
}
