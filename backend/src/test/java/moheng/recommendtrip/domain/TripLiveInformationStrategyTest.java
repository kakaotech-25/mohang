package moheng.recommendtrip.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.recommendtrip.domain.tripfilterstrategy.TripLiveInformationFilterStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TripLiveInformationStrategyTest extends ServiceTestConfig {
    @Autowired
    private TripLiveInformationFilterStrategy tripLiveInformationFilterStrategy;

    @DisplayName("LIVE_INFO 전략 네이밍과 일치하면 전략이 수행된다.")
    @Test
    void LIVE_INFO_전략_네이밍과_일치하면_전략이_수행된다() {
        // given
        String LIVE_INFO = "LIVE_INFO";

        // when, then
        assertTrue(() -> tripLiveInformationFilterStrategy.isMatch(LIVE_INFO));
    }
}
