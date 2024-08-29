package moheng.recommendtrip.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import moheng.config.slice.ServiceTestConfig;
import moheng.recommendtrip.domain.filterinfo.FilterStandardInfo;
import moheng.recommendtrip.domain.filterinfo.LiveInformationFilterInfo;
import moheng.recommendtrip.domain.tripfilterstrategy.TripLiveInformationFilterStrategy;
import moheng.trip.exception.NoExistTripException;
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

    @DisplayName("존재하지 않는 여행지에 기반하여 생활정보를 필터링하려고 하면 예외가 발생한다.")
    @Test
    void 존재하지_않는_여행지에_기반하여_생활정보를_필터링하려고_하면_예외가_발생한다() {
        // given
        long invalidTripId = -1L;
        FilterStandardInfo filterStandardInfo = new LiveInformationFilterInfo(invalidTripId);

        // when, then
        assertThatThrownBy(() -> tripLiveInformationFilterStrategy.execute(filterStandardInfo))
                .isInstanceOf(NoExistTripException.class);
    }
}
