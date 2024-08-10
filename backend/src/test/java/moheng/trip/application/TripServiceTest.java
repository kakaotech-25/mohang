package moheng.trip.application;

import static org.junit.jupiter.api.Assertions.*;

import moheng.config.ServiceTestConfig;
import moheng.trip.domain.Trip;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TripServiceTest extends ServiceTestConfig {
    @Autowired
    private TripService tripService;

    @DisplayName("여행지 정보를 가져온다.")
    @Test
    void 여행지_정보를_가져온다() {
        // given
        tripService.save(new Trip("롯데월드", "서울특별시 송파구", 1000000L,
                "서울 롯데월드는 신나는 여가와 엔터테인먼트를 꿈꾸는 사람들과 갈수록 늘어나는 외국인 관광 활성화를 위해 운영하는 테마파크예요.",
                "https://lotte-world-image.png"));

        // when, then
        assertDoesNotThrow(() -> tripService.findById(1L));
    }
}
