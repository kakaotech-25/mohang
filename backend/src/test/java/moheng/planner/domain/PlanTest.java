package moheng.planner.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import moheng.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
