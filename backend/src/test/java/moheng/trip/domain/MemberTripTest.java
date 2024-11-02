package moheng.trip.domain;

import static moheng.fixture.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTripTest {

    @DisplayName("멤버의 여행지를 생성한다.")
    @Test
    void 멤버의_여행지를_생성한다() {
        // given, when, then
        assertDoesNotThrow(() -> new MemberTrip(하온_기존(), new Trip()));
    }

    @DisplayName("멤버의 여행지 방문횟수를 증가시킨다.")
    @Test
    void 멤버의_여행지_방문횟수를_증가시킨다() {
        MemberTrip memberTrip = new MemberTrip(하온_기존(), new Trip(), 100L);
        memberTrip.incrementVisitedCount();
        assertThat(memberTrip.getVisitedCount()).isEqualTo(101L);
    }
}
