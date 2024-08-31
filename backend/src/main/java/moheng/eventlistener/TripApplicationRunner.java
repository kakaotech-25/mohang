package moheng.eventlistener;

import moheng.trip.domain.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class TripApplicationRunner implements ApplicationRunner {
    private TripRepository tripRepository;

    @Override
    public void run(ApplicationArguments args) {

    }
}
