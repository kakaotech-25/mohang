package moheng.recommendtrip.domain;

import static moheng.fixture.MemberFixtures.하온_기존;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.recommendtrip.domain.tripfilterstrategy.TripPreferredLocationsFilterStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TripPreferredLocationsFilterStrategyTest extends ServiceTestConfig {
    @Autowired
    private TripPreferredLocationsFilterStrategy tripPreferredLocationsFilterStrategy;

    @DisplayName("PREFERRED_LOCATIONS 전략 네이밍에 일치하면 전략이 수행된다.")
    @Test
    void PREFERRED_LOCATIONS_전략_네이밍에_일치하면_전략이_수행된다() {
        // given
        String PREFERRED_LOCATIONS = "PREFERRED_LOCATIONS";

        // when, then
        assertTrue(() -> tripPreferredLocationsFilterStrategy.isMatch(PREFERRED_LOCATIONS));
    }
}
