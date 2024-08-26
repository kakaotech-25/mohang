package moheng.planner.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moheng.member.domain.Member;
import moheng.member.exception.InvalidEmailFormatException;
import moheng.planner.exception.InvalidPlanNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

public class PlanTest {

    @DisplayName("플래너 일정을 생성한다.")
    @Test
    void 플래너_일정을_생성한다() {
        assertDoesNotThrow(() -> new Plan("일정1",
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
                new Plan(planName, LocalDate.of(2024, 8, 26), LocalDate.of(2024, 8, 27), 하온_기존())
        ).isInstanceOf(InvalidPlanNameException.class);
    }
}
