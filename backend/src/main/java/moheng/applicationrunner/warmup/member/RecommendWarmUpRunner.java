package moheng.applicationrunner.warmup.member;

import moheng.recommendtrip.application.RecommendTripService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class RecommendWarmUpRunner implements ApplicationRunner {
    private final RecommendTripService recommendTripService;

    public RecommendWarmUpRunner(final RecommendTripService recommendTripService) {
        this.recommendTripService = recommendTripService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            recommendTripService.findRecommendTripsByModel(1L);
        } catch (Exception e) {

        }
    }
}
