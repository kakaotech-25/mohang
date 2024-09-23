package moheng.applicationrunner.warmup;

import moheng.recommendtrip.application.RecommendTripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile({"dev", "prod"})
@Order(8)
@Component
public class RecommendWarmUpRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(RecommendWarmUpRunner.class);
    private final RecommendTripService recommendTripService;

    public RecommendWarmUpRunner(final RecommendTripService recommendTripService) {
        this.recommendTripService = recommendTripService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            recommendTripService.findRecommendTripsByModel(1L);
        } catch (Exception e) {
            log.info("Latency 개선을 위한 JVM Warm Up 처리중입니다.");
        }
    }
}
