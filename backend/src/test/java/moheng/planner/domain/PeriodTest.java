package moheng.planner.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
}
