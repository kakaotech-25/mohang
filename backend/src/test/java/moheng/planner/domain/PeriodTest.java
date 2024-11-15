package moheng.planner.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import moheng.planner.exception.InvalidDateSequenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @DisplayName("년도와 월이 모두 동일해야 같은 월에 속하는 날짜로 판별한다.")
    @ParameterizedTest
    @CsvSource({
            "2024-11-01, 2024-12-01, true",
            "2024-11-01, 2024-11-30, false",
            "2023-12-31, 2024-01-01, true"
    })
    void 년도와_월이_모두_동일해야_같은_월에_속하는_날짜로_판별한다(final String startDateStr, final String endDateStr, final boolean expected) {
        // given
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        Period period = new Period(startDate, endDate);

        // when
        boolean result = period.isOtherMonthDate();

        // then
        assertEquals(expected, result);
    }
}
