package moheng.trip.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import moheng.trip.exception.InvalidTripNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TripTest {

    @DisplayName("여행지 이름의 길이가 유효하지 않다면 예외가 발생한다.")
    @Test
    void 여행지_이름의_길이가_유효하지_않다면_예외가_발생한다() {
        String name = "";

        assertThatThrownBy(() -> new Trip(name, "서울특별시 송파구", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.",
                "https://lotte-world-image.png"))
                .isInstanceOf(InvalidTripNameException.class);
    }
}
