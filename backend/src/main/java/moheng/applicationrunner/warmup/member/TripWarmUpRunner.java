package moheng.applicationrunner.warmup.member;

import moheng.trip.application.TripService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class TripWarmUpRunner implements ApplicationRunner {
    private final TripService tripService;

    public TripWarmUpRunner(final TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        tripService.findWithSimilarOtherTrips(1L, 1L);
    }
}
