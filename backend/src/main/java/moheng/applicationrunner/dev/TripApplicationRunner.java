package moheng.applicationrunner.dev;

import moheng.trip.domain.Trip;
import moheng.trip.domain.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
public class TripApplicationRunner implements ApplicationRunner {
    private final TripRepository tripRepository;

    public TripApplicationRunner(final TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        tripRepository.save(new Trip("여행지1", "서울특별시 송파구1", 1L, "여행지 설명1",
                "https://lotte-world.ong"));

        tripRepository.save(new Trip("여행지2", "서울특별시 송파구2", 2L, "여행지 설명2",
                "https://lotte-world.ong"));

        tripRepository.save(new Trip("여행지3", "서울특별시 송파구3", 3L, "여행지 설명3",
                "https://lotte-world.ong"));

        tripRepository.save(new Trip("여행지4", "서울특별시 송파구4", 4L, "여행지 설명4",
                "https://lotte-world.ong"));

        tripRepository.save(new Trip("여행지5", "서울특별시 송파구5", 5L, "여행지 설명5",
                "https://lotte-world.ong"));

        tripRepository.save(new Trip("여행지6", "서울특별시 송파구6", 6L, "여행지 설명6",
                "https://lotte-world.ong"));

        tripRepository.save(new Trip("여행지7", "서울특별시 송파구7", 7L, "여행지 설명7",
                "https://lotte-world.ong"));

        tripRepository.save(new Trip("여행지8", "서울특별시 송파구8", 8L, "여행지 설명8",
                "https://lotte-world.ong"));

        tripRepository.save(new Trip("여행지9", "서울특별시 송파구9", 9L, "여행지 설명9",
                "https://lotte-world.ong"));

        tripRepository.save(new Trip("여행지10", "서울특별시 송파구10", 10L, "여행지 설명10",
                "https://lotte-world.ong"));

    }
}
