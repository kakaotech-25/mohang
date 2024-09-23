package moheng.applicationrunner.warmup.member;

import moheng.trip.application.TripService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Profile({"dev", "prod"})
@Order(6)
@Component
public class TripWarmUpRunner implements ApplicationRunner {
    private final TripService tripService;

    public TripWarmUpRunner(final TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            tripService.findWithSimilarOtherTrips(1L, 1L);
        } catch (Exception e) {

        }
    }
}
