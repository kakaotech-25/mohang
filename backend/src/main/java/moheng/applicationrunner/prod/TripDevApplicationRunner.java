package moheng.applicationrunner.prod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import moheng.applicationrunner.dto.TripRunner;
import moheng.trip.domain.Trip;
import moheng.trip.domain.repository.TripRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TripDevApplicationRunner implements ApplicationRunner {
    private final TripRepository tripRepository;

    public TripDevApplicationRunner(final TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(tripRepository.count() == 0) {
            final Resource resource1 = new ClassPathResource("trip1.json");
            final Resource resource2 = new ClassPathResource("trip2.json");
            final ObjectMapper objectMapper = new ObjectMapper();

            final List<TripRunner> tripRunners = new ArrayList<>();
            tripRunners.addAll(findTripRunnersByResource(resource1, objectMapper));
            tripRunners.addAll(findTripRunnersByResource(resource2, objectMapper));

            for(final TripRunner tripRunner : tripRunners) {
                final Long contentId = tripRunner.getContentid();
                final String title = tripRunner.getTitle();
                final String placeName = tripRunner.getArea_sigungu_combined();
                final String tripImageUrl = tripRunner.getFirstimage();
                final Double mapX = tripRunner.getMapx();
                final Double mapY = tripRunner.getMapy();
                final String description = tripRunner.getOverview();

                tripRepository.save(new Trip(title, placeName, contentId, description, tripImageUrl, mapX, mapY));
            }
        }
        System.out.println(tripRepository.count());
    }

    private List<TripRunner> findTripRunnersByResource(final Resource resource, final ObjectMapper objectMapper) throws IOException {
        return objectMapper.readValue(resource.getInputStream(), new TypeReference<List<TripRunner>>() {});
    }
}
